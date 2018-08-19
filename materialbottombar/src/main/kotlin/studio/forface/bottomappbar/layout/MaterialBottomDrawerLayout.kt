@file:Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName")

package studio.forface.bottomappbar.layout

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
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
import studio.forface.bottomappbar.utils.children
import studio.forface.bottomappbar.utils.dpToPixels
import studio.forface.bottomappbar.utils.findChild
import studio.forface.bottomappbar.utils.show
import studio.forface.bottomappbar.view.PanelView
import timber.log.Timber
import java.util.*

class MaterialBottomDrawerLayout @JvmOverloads constructor (
        context: Context, val attrs: AttributeSet? = null, val defStyleAttr: Int = 0
) : CoordinatorLayout( context, attrs, defStyleAttr ) {

    private val MIN_DRAG_THRESHOLD = dpToPixels(10f )
    private val DRAG_THRESHOLD by lazy { height / 10 }

    private val BOTTOM_BAR_RANGE get() =
        bottomAppBar?.let {
            it.y .. height.toFloat()
        } ?: 0f .. 0f

    val bottomAppBar    get() = findChild<MaterialBottomAppBar>()
    val fab             get() = findChild<FloatingActionButton>()
    //val topAppBar       get() = findChild<AppBarLayout>()

    private val bottomAppBarInitialY by lazy { bottomAppBar?.y ?: height.toFloat() }
    private val matchDrawerY get() = height / 3f

    private var viewsAnimator: Animator? = null
    private var drawerHeaderColor: Int? = null

    var panels = mutableMapOf<Int, MaterialPanel>()

    var drawerPanelId = 0
    val drawerPanel get() = panels[drawerPanelId]?.panelView

    private val newDrawerPanelId: Int get() {
        val random = Random()
        var newId = 0
        while ( panels.keys.contains( newId ) ) { newId = random.nextInt() }
        return newId
    }

    var drawer: MaterialDrawer = MaterialDrawer()
        get() = panels[drawerPanelId] as MaterialDrawer
        set( value ) = value.run {
            addPanel( field, drawerPanelId,true )
        }

    private val drawerRecyclerView get() = drawerPanel?.body as? RecyclerView

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

        val panelView = PanelView(this, materialPanel )
        addView( panelView )
        panelView.layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        panelView.y = height.toFloat()
        materialPanel.panelView = panelView
        panels[id] = materialPanel

        materialPanel.run {
            observe { newPanel, change -> when( change ) {
                is MaterialPanel.Change.HEADER ->  { setHeader( newPanel.header, panelView ) }
                is MaterialPanel.Change.BODY ->    { setBody(   newPanel.body,   panelView ) }
                is MaterialPanel.Change.PANEL->    { change.id }
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

    fun closeDrawer() { closePanel() }

    fun closePanel() {
        flyBar( Fly.BOTTOM )
    }

    private var grabPanel: PanelView? = null

    private fun setHeader( header: MaterialPanel.IHeader?, panelView: PanelView ) {
        ( header as? MaterialPanel.AbsHeader<*> )?.let {
            header.applyIconTo( panelView.header.header_icon )

            header.applyTitleTo( panelView.header.header_title )
            header.titleColorHolder.applyToDrawable( panelView.header.header_close )

            drawerHeaderColor = header.backgroundColorHolder.resolveColor( context )

        } ?: ( header as? MaterialPanel.CustomHeader )?.let {
            panelView.header = header.contentView
        }
    }

    private fun setBody( body: MaterialPanel.IBody?, panelView: PanelView ) {
        ( body as? MaterialPanel.AbsBody<*> )?.let {
            if ( body.selectionColorHolder.resolveColor( context ) == null) {
                val color = drawerHeaderColor ?: Color.GRAY
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
                grabPanel = drawerPanel
                flyBar( Fly.MATCH_DRAWER )
            }

            /*panels.forEach {
                val panelView = PanelView(this, it.value )
                addView( panelView )
                panelView.layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
                panelView.y = 555f
                it.value.panelView = panelView
            }*/

            doOnNextLayout {
                /*panels.forEach {
                    it.value.panelView?.y = height.toFloat()
                }*/

                /*if ( panels.isEmpty() ) */setViewsY( bottomAppBarInitialY )
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

        val inRange = downY in BOTTOM_BAR_RANGE || event.y in BOTTOM_BAR_RANGE

        if ( actionDown ) {
            downY = event.y
            downTimestamp = System.currentTimeMillis()
        }

        val direction = downY.compareTo( event.y )
        val shouldScrollDrawerRecyclerView =
                event.action == MotionEvent.ACTION_MOVE && bottomAppBar!!.y < 1 &&
                        drawerRecyclerView?.canScrollVertically( direction ) ?: false

        if ( ! shouldScrollDrawerRecyclerView && inRange ) {
            if ( onTouchEvent(event) )
                consumedTimestamp = System.currentTimeMillis()

        } else if ( ! inRange )
            flyBar(Fly.BOTTOM)

        if ( actionUp ) {
            val moved = Math.abs( downY - event.y ) > MIN_DRAG_THRESHOLD

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
                        drawerRecyclerView?.canScrollVertically( direction ) ?: false

        if ( shouldScrollDrawerRecyclerView ) return false

        return when( event.action ) {
            MotionEvent.ACTION_DOWN -> onDown( event )
            MotionEvent.ACTION_MOVE -> onMove( event )
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> releaseBar( event )
            else -> super.onTouchEvent( event )
        }
    }

    fun grabBar() {
        grabPanel = drawerPanel
        draggingBar = true
    }
    fun releaseBar( event: MotionEvent ) = bottomAppBar?.let {
        if ( draggingBar ) {
            draggingBar = false

            val inThreshold = Math.abs( event.y - downY ) > DRAG_THRESHOLD

            if (inThreshold) {
                val isDraggingUp = event.y < downY

                val fly = if ( ! isDraggingUp ) Fly.BOTTOM
                else if (it.y < matchDrawerY) Fly.TOP
                else Fly.MATCH_DRAWER

                flyBar( fly )

            } else flyBar( lastFly )

            true

        } else true

    } ?: false

    fun dragBar( y: Float ) = bottomAppBar?.let {
        val toY = ( bottomBarDownY - ( downY - y ) )
                .coerceAtLeast(0f )
                .coerceAtMost( bottomAppBarInitialY )
        setViewsY( toY )
    }

    fun onDown( event: MotionEvent ) = if ( event.y in BOTTOM_BAR_RANGE ) {
        bottomBarDownY = bottomAppBar?.y ?: 0f
        true
    } else false


    fun onMove( event: MotionEvent ) = when {
        draggingBar -> { dragBar( event.y ); true }
        downY in BOTTOM_BAR_RANGE -> { grabBar(); true }
        else -> false
    }

    enum class Fly { TOP, BOTTOM, MATCH_DRAWER }
    private var lastFly = Fly.BOTTOM
    private fun flyBar( fly: Fly ) {
        lastFly = fly
        bottomAppBar?.let {
            val toY = when( fly ) {
                Fly.TOP ->          0f
                Fly.BOTTOM ->       bottomAppBarInitialY
                Fly.MATCH_DRAWER -> matchDrawerY
            }
            animateViewsY( toY )
        }
    }

    private var hasFab = fab?.isVisible == true

    fun animateViewsY( toY: Float ) {
        viewsAnimator?.cancel()

        val fromY = bottomAppBar!!.y
        if ( fromY == bottomAppBarInitialY ) hasFab = fab?.isVisible == true

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
    private fun setViewsY( y: Float ) {
        if ( y < 0f ) return

        Timber.d("$grabPanel" )

        bottomAppBar!!.y =  y
        grabPanel?.y =      y

        val height = height - bottomAppBar!!.height

        //val totalPercentage =   1f / ( height / y )
        val topPercentage =     1f / ( matchDrawerY / y.coerceAtMost( matchDrawerY ) )
        val bottomPercentage =  1f / ( ( height - matchDrawerY ) / ( y - matchDrawerY ).coerceAtLeast(0f ) )

        bottomAppBar!!.cornersInterpolation =   topPercentage

        if ( bottomAppBarInitialColor == null ) bottomAppBarInitialColor =
                ( bottomAppBar?.background as? MaterialShapeDrawable )?.tintList?.defaultColor

        bottomAppBarInitialColor?.let { blendFrom ->
            val blendTo = drawerHeaderColor ?: blendFrom
            val blend = ColorUtils.blendARGB(
                    blendFrom, blendTo, 1f - bottomPercentage
            )
            ColorHolder( color = blend ).applyToBackground( bottomAppBar!! )
        }

        val isInitialState = y == bottomAppBarInitialY

        if ( isInitialState ) drawerRecyclerView?.scrollToPosition( 0 )

        //drawerHeaderColorHolder?.applyToBackground( bottomAppBar!! )
        bottomAppBar!!.children.forEach {
            it.alpha = bottomPercentage
            it.isClickable =   isInitialState
            it.isEnabled =     isInitialState
        }
        grabPanel?.header?.children?.forEach {
            it.alpha = 1f - bottomPercentage
            it.isClickable =   ! isInitialState
            it.isEnabled =     ! isInitialState
        }

        fab?.show(isInitialState && hasFab )
    }

}