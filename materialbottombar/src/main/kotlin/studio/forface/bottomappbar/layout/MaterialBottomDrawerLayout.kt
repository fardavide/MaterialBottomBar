@file:Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName")

package studio.forface.bottomappbar.layout

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import androidx.core.graphics.ColorUtils
import androidx.core.view.children
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.android.synthetic.main.drawer_header.view.*
import studio.forface.bottomappbar.appbar.MaterialBottomAppBar
import studio.forface.bottomappbar.drawer.MaterialDrawer
import studio.forface.bottomappbar.panels.MaterialPanel
import studio.forface.bottomappbar.panels.adapter.PanelBodyAdapter
import studio.forface.bottomappbar.panels.holders.ColorHolder
import studio.forface.bottomappbar.utils.*
import studio.forface.bottomappbar.view.PanelView
import timber.log.Timber
import java.util.*

class MaterialBottomDrawerLayout @JvmOverloads constructor (
        context: Context, val attrs: AttributeSet? = null, val defStyleAttr: Int = 0
) : CoordinatorLayout( context, attrs, defStyleAttr ) {

    /**
     * The minimum dragging distance after the Layout intercept the [MotionEvent].
     * If the minimum distance has been consumed from the [MotionEvent.ACTION_DOWN] to the
     * [MotionEvent.ACTION_UP] the layout will be intercept the [MotionEvent] blocking the delivery
     * to the other Views, else the [MotionEvent] event will be delivered.
     * @see onInterceptTouchEvent
     */
    private val MIN_INTERCEPT_DRAG_THRESHOLD = dpToPixels(10f )

    /**
     * The minimum dragging distance before the [bottomAppBar] backs to its old position.
     * If the minimum distance has been consumed the [bottomAppBar] will [flyBar] to the closest
     * position, else it will [flyBar] back to [lastFly] position.
     */
    private val MIN_FLY_DRAG_THRESHOLD by lazy { height / 10 }

    val bottomAppBar    get() = findChildType<MaterialBottomAppBar>()
    val fab             get() = findChildType<FloatingActionButton>()
    //val topAppBar       get() = findChildType<AppBarLayout>()

    /**
     * If [bottomAppBar] is not null, it represents the [ClosedFloatingPointRange] that goes from
     * [bottomAppBar] [MaterialBottomAppBar.getY] until the end of the layout ( [getHeight] ),
     * else it's 0..0.
     */
    private val bottomBarRange get() =
        bottomAppBar?.let {
            it.y .. height.toFloat()
        } ?: 0f .. 0f

    /**
     * The [View.getY] of the [bottomAppBar] initial position ( bottom ).
     * if [bottomAppBar] [MaterialBottomAppBar.getY] is not null, we set a default value which
     * is the [getHeight] of the layout and we will try the get the right Y, every time the
     * parameter is called, until it is set, after that it is set it will be un-mutable.
     */
    private val bottomBarInitialY by retryIfDefaultLazy( height.toFloat() ) { bottomAppBar?.y }
    private val matchPanelY get() = if ( draggingPanelView?.wrapToContent == true ) {
        ( height - draggingPanelView!!.contentHeight.toFloat() ).coerceAtLeast(0f )
    } else height / 3f

    private val isBarInInitialState get() = bottomAppBar?.y == bottomBarInitialY

    private var viewsAnimator: Animator? = null

    var panels = mutableMapOf<Int, MaterialPanel>()

    var drawerPanelId = 0
    val drawerPanel get() = panels[drawerPanelId]?.panelView

    private val newDrawerPanelId: Int get() {
        val random = Random()
        var newId = 0
        while ( panels.keys.contains( newId ) ) { newId = random.nextInt() }
        return newId
    }

    var drawer: MaterialDrawer
        get() = panels[drawerPanelId] as MaterialDrawer
        set( value ) = value.run {
            addPanel( value, drawerPanelId,true )
        }

    private val draggingPanelRecyclerView: RecyclerView? get() =
        draggingPanelView?.body as? RecyclerView
                ?: ( draggingPanelView?.body as? ViewGroup )?.findChildType()

    fun addPanel( materialPanel: MaterialPanel, id: Int ) {
        addPanel( materialPanel, id,false )
    }

    private fun addPanel( materialPanel: MaterialPanel, id: Int, isDrawer: Boolean ) {
        if ( ! isDrawer && id == drawerPanelId ) {
            val tempDrawer = panels[drawerPanelId]
            drawerPanelId = newDrawerPanelId
            tempDrawer?.let { panels[drawerPanelId] = tempDrawer }
        }

        removePanel( id )
        if ( isDrawer ) { drawerPanelId = id }

        val panelView = PanelView(this, materialPanel)
        addView( panelView )
        panelView.layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        panelView.y = height.toFloat()
        materialPanel.panelView = panelView
        panels[id] = materialPanel

        materialPanel.run {
            observe { newPanel, change -> when( change ) {
                is MaterialPanel.Change.HEADER ->  { setHeader( newPanel.header, panelView ) }
                is MaterialPanel.Change.BODY ->    { setBody(   newPanel.body,   panelView ) }
                is MaterialPanel.Change.PANEL->    { addPanel( materialPanel, id, isDrawer ) }
            } }

            setHeader( header, panelView )
            setBody(   body,   panelView )
        }
    }

    fun removePanel( id: Int ) {
        panels[id]?.let {
            it.deleteObservers()
            removeView( it.panelView )
        }
        panels.remove( id )
    }

    fun openDrawer() { openPanel( drawerPanelId ) }

    fun openPanel( id: Int ) {
        grabPanel( id )
        flyBar( Fly.MATCH_DRAWER )
    }

    fun closeDrawer() { closePanel() }

    fun closePanel() {
        flyBar( Fly.BOTTOM )
    }

    private var draggingPanelId: Int = Int.MIN_VALUE // TODO: test only
    private var draggingPanelView: PanelView? = null
    private var draggingPanelHeaderColor: Int? = null

    private fun grabPanel( id: Int ) {
        draggingPanelId = id // TODO: test only
        val draggingPanel = panels[id]
        draggingPanelView = draggingPanel?.panelView
        draggingPanelHeaderColor = ( draggingPanel?.header as? MaterialPanel.AbsHeader<*> )
                ?.backgroundColorHolder?.resolveColor( context )
    }


    private fun setHeader( header: MaterialPanel.IHeader?, panelView: PanelView ) {
        panelView.setHeader(this, header )
        ( header as? MaterialPanel.AbsHeader<*> )?.let {
            header.applyIconTo( panelView.header.header_icon )

            header.applyTitleTo( panelView.header.header_title )
            header.titleColorHolder.applyToDrawable( panelView.header.header_close )

        } ?: ( header as? MaterialPanel.CustomHeader )?.let {
            panelView.header = header.contentView
        }
    }

    private fun setBody( body: MaterialPanel.IBody?, panelView: PanelView ) {
        panelView.setBody( body )
        ( body as? MaterialPanel.AbsBody<*> )?.let {
            if ( body.selectionColorHolder.resolveColor( context ) == null) {
                val color = draggingPanelHeaderColor ?: Color.GRAY
                body.selectionColor( color )
            }

            val bodyRecyclerView = panelView.body as RecyclerView
            bodyRecyclerView.layoutManager = LinearLayoutManager( context )
            val adapter = PanelBodyAdapter( body )
            body.addObserver { _, _ -> adapter.notifyDataSetChanged() }
            bodyRecyclerView.adapter = adapter

        } ?: ( body as? MaterialPanel.CustomBody )?.let {
            panelView.body = body.contentView
        }
    }

    init {
        doOnPreDraw {
            bottomAppBar?.setNavigationOnClickListener {
                grabPanel( drawerPanelId )
                flyBar( Fly.MATCH_DRAWER )
            }

            panels.forEach { val panelView = it.value.panelView
                panelView?.y = height.toFloat()
                bottomAppBar?.height?.let { barHeight ->
                    panelView?.header?.layoutParams?.height = barHeight
                }
            }

            doOnNextLayout {
                /*panels.forEach {
                    it.value.panelView?.y = height.toFloat()
                }*/

                /*if ( panels.isEmpty() ) */setViewsY( bottomBarInitialY )
            }
        }
    }

    private var downY = 0f
    private var bottomBarDownY = 0f
    private var draggingBar = false

    var downTimestamp = 0L
    var consumedTimestamp = 0L

    override fun onInterceptTouchEvent( event: MotionEvent ): Boolean {
        val actionDown = event.action == MotionEvent.ACTION_DOWN
        val actionUp = event.action == MotionEvent.ACTION_UP ||
                event.action == MotionEvent.ACTION_CANCEL

        val inRange = downY in bottomBarRange || event.y in bottomBarRange

        if ( actionDown ) {
            downY = event.y
            downTimestamp = System.currentTimeMillis()
        }

        val direction = downY.compareTo( event.y )
        val shouldScrollDrawerRecyclerView =
                event.action == MotionEvent.ACTION_MOVE && bottomAppBar!!.y < 1 &&
                        draggingPanelRecyclerView?.canScrollVertically( direction ) ?: false

        if ( ! shouldScrollDrawerRecyclerView && inRange ) {
            if ( onTouchEvent(event) )
                consumedTimestamp = System.currentTimeMillis()

        } else if ( ! inRange )
            flyBar( Fly.BOTTOM )

        if ( actionUp ) {
            val moved = Math.abs( downY - event.y ) > MIN_INTERCEPT_DRAG_THRESHOLD

            downY = 0f
            return moved
        }

        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent( event: MotionEvent ): Boolean {

        val direction = downY.compareTo( event.y )
        val shouldScrollDrawerRecyclerView =
                event.action == MotionEvent.ACTION_MOVE && bottomAppBar!!.y < 1 &&
                        draggingPanelRecyclerView?.canScrollVertically( direction ) ?: false

        if ( shouldScrollDrawerRecyclerView ) return false

        return when( event.action ) {
            MotionEvent.ACTION_DOWN -> onDown( event )
            MotionEvent.ACTION_MOVE -> onMove( event )
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> releaseBar( event )
            else -> super.onTouchEvent( event )
        }
    }

    fun grabBar() {
        if ( isBarInInitialState )
            grabPanel( drawerPanelId )
        draggingBar = true
    }
    fun releaseBar( event: MotionEvent ) = bottomAppBar?.let {
        if ( draggingBar ) {
            draggingBar = false

            val inThreshold = Math.abs( event.y - downY ) > MIN_FLY_DRAG_THRESHOLD

            if (inThreshold) {
                val isDraggingUp = event.y < downY

                val fly = if ( ! isDraggingUp ) Fly.BOTTOM
                else if ( it.y < matchPanelY ) Fly.TOP
                else Fly.MATCH_DRAWER

                flyBar( fly )

            } else flyBar( lastFly )

            true

        } else true

    } ?: false

    fun dragBar( y: Float ) = bottomAppBar?.let {
        val toY = ( bottomBarDownY - ( downY - y ) )
                .coerceAtLeast(0f )
                .coerceAtMost( bottomBarInitialY )
        setViewsY( toY )
    }

    fun onDown( event: MotionEvent ) = if ( event.y in bottomBarRange ) {
        bottomBarDownY = bottomAppBar?.y ?: 0f
        true
    } else false


    fun onMove( event: MotionEvent ) = when {
        draggingBar -> { dragBar( event.y ); true }
        downY in bottomBarRange -> { grabBar(); true }
        else -> false
    }

    enum class Fly { TOP, BOTTOM, MATCH_DRAWER }
    private var lastFly = Fly.BOTTOM
    private fun flyBar( fly: Fly ) {
        lastFly = fly
        bottomAppBar?.let {
            val toY = when( fly ) {
                Fly.TOP ->          0f
                Fly.BOTTOM ->       bottomBarInitialY
                Fly.MATCH_DRAWER -> matchPanelY
            }
            animateViewsY( toY )
        }
    }

    private var hasFab = fab?.isVisible == true

    fun animateViewsY( toY: Float ) {
        viewsAnimator?.cancel()

        val fromY = bottomAppBar!!.y
        if ( fromY == bottomBarInitialY ) hasFab = fab?.isVisible == true

        val duration = Math.abs( fromY - toY ) / 2
        viewsAnimator = ValueAnimator.ofFloat( fromY, toY ).apply {
            addUpdateListener {
                setViewsY( it.animatedValue as Float )
            }
            this.duration = duration.toLong()
        }

        viewsAnimator!!.doOnEnd { viewsAnimator = null }
        viewsAnimator!!.start()
    }

    private var bottomAppBarInitialColor: Int? = null
    @SuppressLint("ResourceType")
    private fun setViewsY(y: Float ) {
        if ( y < 0f ) return

        Timber.d("$draggingPanelId ... $drawerPanelId ... ${panels.joinToString { "${it.key}" }}" )

        bottomAppBar!!.y =  y
        draggingPanelView?.y =      y

        val height = height - bottomAppBar!!.height

        //val totalPercentage =   1f / ( height / y )
        val topPercentage =     1f / ( matchPanelY / y.coerceAtMost( matchPanelY ) )
        val bottomPercentage =  1f / ( ( height - matchPanelY ) / ( y - matchPanelY ).coerceAtLeast(0f ) )

        bottomAppBar!!.cornersInterpolation =   topPercentage

        if ( bottomAppBarInitialColor == null ) bottomAppBarInitialColor =
                ( bottomAppBar?.background as? MaterialShapeDrawable )?.tintList?.defaultColor

        bottomAppBarInitialColor?.let { blendFrom ->
            val blendTo = draggingPanelHeaderColor ?: blendFrom
            val blend = ColorUtils.blendARGB(
                    blendFrom, blendTo, 1f - bottomPercentage
            )
            ColorHolder( color = blend ).applyToBackground( bottomAppBar!! )
        }

        if ( isBarInInitialState ) {
            panels.forEach { it.value.panelView?.y = height.toFloat() }
            draggingPanelRecyclerView?.scrollToPosition( 0 )
        }

        //drawerHeaderColorHolder?.applyToBackground( bottomAppBar!! )
        bottomAppBar!!.children.forEach {
            it.alpha = bottomPercentage
            it.isClickable =   isBarInInitialState
            it.isEnabled =     isBarInInitialState
        }
        draggingPanelView?.fadeHeader(1f - bottomPercentage, ! isBarInInitialState )

        fab?.show(isBarInInitialState && hasFab )
    }

}