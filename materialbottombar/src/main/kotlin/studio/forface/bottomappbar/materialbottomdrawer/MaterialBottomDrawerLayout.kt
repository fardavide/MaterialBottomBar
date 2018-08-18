@file:Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName")

package studio.forface.bottomappbar.materialbottomdrawer

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
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
import studio.forface.bottomappbar.materialbottomappbar.MaterialBottomAppBar
import studio.forface.bottomappbar.materialpanels.adapter.PanelBodyAdapter
import studio.forface.bottomappbar.materialbottomdrawer.drawer.MaterialDrawer
import studio.forface.bottomappbar.materialpanels.holders.ColorHolder
import studio.forface.bottomappbar.utils.dpToPixels
import studio.forface.bottomappbar.utils.elevationCompat
import studio.forface.bottomappbar.utils.findChild
import studio.forface.bottomappbar.utils.show
import studio.forface.materialbottombar.bottomappbar.R

class MaterialBottomDrawerLayout @JvmOverloads constructor (
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
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

    var drawer: MaterialDrawer = MaterialDrawer()
        set( value ) = value.run {
            field.body?.deleteObservers()
            field = this

            observe { newDrawer, change -> when( change ) {
                is MaterialDrawer.Change.HEADER ->  { setHeader( newDrawer.header ) }
                is MaterialDrawer.Change.BODY ->    { setBody(   newDrawer.body   ) }
                is MaterialDrawer.Change.PANEL->    { change.id }
            } }

            setHeader( header )
            setBody(   body   )
        }

    private fun setHeader( header: MaterialDrawer.Header? ) { header ?: return
        header.applyIconTo( drawerHeader.header_icon )

        header.applyTitleTo( drawerHeader.header_title )
        header.titleColorHolder.applyToDrawable( drawerHeader.header_close )

        drawerHeaderColor = header.backgroundColorHolder.resolveColor( context )
    }

    private fun setBody( body: MaterialDrawer.Body? ) { body ?: return
        if ( body.selectionColorHolder.resolveColor( context ) == null ) {
            val color = drawerHeaderColor ?: Color.GRAY
            body.selectionColor( color )
        }

        drawerRecyclerView.layoutManager = LinearLayoutManager( context )
        val adapter = PanelBodyAdapter( body )
        body.addObserver { _, _ -> adapter.notifyDataSetChanged() }
        drawerRecyclerView.adapter = adapter
    }

    private val drawerHeader by lazy {
        LayoutInflater.from( context )
                .inflate( R.layout.drawer_header,this, false )
                .apply {
                    header_shadow.elevationCompat = 14f
                    elevationCompat = 8f
                    header_close.setOnClickListener { flyBar( Fly.BOTTOM ) }
                } as ConstraintLayout
    }

    private val drawerRecyclerView by lazy {
        LayoutInflater.from( context )
                .inflate( R.layout.drawer_body,this, false )
                as RecyclerView
    }

    private val drawerBottomBackground by lazy {
        View( context ).apply {
            setBackgroundColor( Color.WHITE )
            elevationCompat = 1f
        }
    }

    init {
        doOnPreDraw {
            bottomAppBar?.setNavigationOnClickListener { flyBar( Fly.TOP ) }

            bottomAppBar?.let { bar ->
                bar.setNavigationOnClickListener { flyBar( Fly.MATCH_DRAWER ) }
                addView( drawerHeader )
                drawerHeader.layoutParams.height = bar.height

                addView( drawerRecyclerView )
                drawerRecyclerView.layoutParams.width = CoordinatorLayout.LayoutParams.MATCH_PARENT

                addView( drawerBottomBackground )
                drawerBottomBackground.layoutParams.height = CoordinatorLayout.LayoutParams.MATCH_PARENT

                doOnNextLayout { setViewsY( bottomAppBarInitialY ) }
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
                        drawerRecyclerView.canScrollVertically( direction )

        if ( ! shouldScrollDrawerRecyclerView && inRange ) {
            if ( onTouchEvent(event) )
                consumedTimestamp = System.currentTimeMillis()

        } else if ( ! inRange )
            flyBar( Fly.BOTTOM )

        if ( actionUp ) {
            val moved = Math.abs( downY - event.y ) > MIN_DRAG_THRESHOLD

            downY = 0f
            return moved
        }

        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent ): Boolean {

        val direction = downY.compareTo( event.y )
        val shouldScrollDrawerRecyclerView =
                event.action == MotionEvent.ACTION_MOVE && bottomAppBar!!.y < 1 &&
                        drawerRecyclerView.canScrollVertically( direction )

        if ( shouldScrollDrawerRecyclerView ) return false

        return when( event.action ) {
            MotionEvent.ACTION_DOWN -> onDown( event )
            MotionEvent.ACTION_MOVE -> onMove( event )
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> releaseBar( event )
            else -> super.onTouchEvent( event )
        }
    }

    fun grabBar() {
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

        bottomAppBar!!.y =      y
        drawerHeader.y =        y
        drawerRecyclerView.y =  y + bottomAppBar!!.height
        drawerBottomBackground.y = drawerRecyclerView.y + drawerRecyclerView.height

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

        if ( isInitialState ) drawerRecyclerView.scrollToPosition(0 )

        //drawerHeaderColorHolder?.applyToBackground( bottomAppBar!! )
        bottomAppBar!!.children.forEach {
            it.alpha = bottomPercentage
            it.isClickable =   isInitialState
            it.isEnabled =     isInitialState
        }
        drawerHeader.children.forEach {
            it.alpha = 1f - bottomPercentage
            it.isClickable =   ! isInitialState
            it.isEnabled =     ! isInitialState
        }

        fab?.show(isInitialState && hasFab )
    }

}

