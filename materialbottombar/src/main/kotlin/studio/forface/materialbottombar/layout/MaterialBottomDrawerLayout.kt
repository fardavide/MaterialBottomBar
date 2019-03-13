@file:Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName", "unused")

package studio.forface.materialbottombar.layout

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import androidx.core.graphics.ColorUtils
import androidx.core.view.children
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.android.synthetic.main.drawer_header.view.*
import studio.forface.materialbottombar.appbar.MaterialBottomAppBar
import studio.forface.materialbottombar.layout.MaterialBottomDrawerLayout.Fly
import studio.forface.materialbottombar.panels.AbsMaterialPanel
import studio.forface.materialbottombar.panels.MaterialPanel
import studio.forface.materialbottombar.panels.adapter.PanelBodyAdapter
import studio.forface.materialbottombar.panels.holders.ColorHolder
import studio.forface.materialbottombar.utils.*
import studio.forface.materialbottombar.view.PanelView
import java.util.*
import kotlin.collections.set

/**
 * @author Davide Giuseppe Farella
 * A [CoordinatorLayout] that can contains a custom [MaterialBottomAppBar] with [MaterialPanel]s
 */
class MaterialBottomDrawerLayout @JvmOverloads constructor (
        context: Context, val attrs: AttributeSet? = null, val defStyleAttr: Int = 0
) : CoordinatorLayout( context, attrs, defStyleAttr ) {


    /* ===================================== C O R E ============================================ */

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
    @Suppress("RemoveExplicitTypeArguments")
    val topAppBar       get() = findChildType<AppBarLayout>()

    /**
     * The [bottomAppBar] background color
     * if [bottomAppBar] [MaterialBottomAppBar.getBackground] [MaterialShapeDrawable.tintList]
     * [ColorStateList.getDefaultColor] is not null, we set it as value, else keep trying
     * at every call and set when it isn't null
     *
     * ACTUALLY IT DOESN'T SUPPORT COLOR CHANGE AT RUNTIME.
     */
    private val bottomBarInitialColor by retryIfNullLazy {
        ( bottomAppBar?.background as? MaterialShapeDrawable )?.tintList?.defaultColor
    }

    /**
     * GET the [ClosedFloatingPointRange] that goes from [bottomAppBar] [MaterialBottomAppBar.getY]
     * until the end of the layout ( [getHeight] ). If [bottomAppBar] is not null, it will be 0..0.
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

    /** @return whether the [bottomAppBar] is at its initial state ( [Gravity.BOTTOM] ) */
    private val isBarInInitialState get() = bottomAppBar?.y == bottomBarInitialY

    /** A [MutableMap] composed by the Panel ID and the [MaterialPanel] */
    var panels = mutableMapOf<Int, AbsMaterialPanel>()

    /** The id of [MaterialPanel] that is currently being dragged by the user */
    private var draggingPanelId: Int? = null

    /** The [PanelView] that is currently being dragged by the user */
    private var draggingPanelView: PanelView? = null

    /**
     * It represents the Background Color of the current dragging Panel.
     * If no Panel is being dragged it is null.
     * If the [AbsMaterialPanel.header] is [AbsMaterialPanel.BaseHeader], it will be
     * [ColorHolder.resolveColor];
     * If the [AbsMaterialPanel.header] is [AbsMaterialPanel.CustomHeader] and the
     * [View.getBackground] [AbsMaterialPanel.CustomHeader.contentView] is [ColorDrawable], it will
     * be [ColorDrawable.getColor];
     * Else it will be null.
     */
    private var draggingPanelHeaderColor: Int? = null

    /**
     * GET the Y position where the [bottomAppBar] and [draggingPanelView] should stop when flying
     * to [Fly.MATCH_PANEL].
     * If [draggingPanelView] is not null and [PanelView.wrapToContent] is true, it will be the
     * [getHeight] minus the [draggingPanelView] [PanelView.contentHeight] but not less that 0f,
     * else it will be [getHeight] / 3f.
     */
    private val matchPanelY get() = if ( draggingPanelView?.wrapToContent == true ) {
        ( height - draggingPanelView!!.contentHeight.toFloat() ).coerceAtLeast(0f )
    } else height / 3f

    /**
     * GET a [RecyclerView] if the [draggingPanelView] [AbsMaterialPanel.body] IS or CONTAINS a
     * [RecyclerView].
     * We need it for understand if a scroll event should move the [draggingPanelView] or
     * scroll the [RecyclerView].
     */
    private val draggingPanelRecyclerView: RecyclerView? get() =
        draggingPanelView?.body as? RecyclerView
                ?: ( draggingPanelView?.body as? ViewGroup )?.findChildType()

    /** @return The Drawer Menu */
    var drawer: AbsMaterialPanel?
        get() = panels[drawerPanelId]
        set( value ) =
            value?.let { setPanel(drawerPanelId, it, true) }
                    ?: removePanel( drawerPanelId )


    /** The ID of the [drawerPanel] */
    private var drawerPanelId = 0

    /** The [AbsMaterialPanel] which represent the [drawer] [AbsMaterialPanel.panelView] */
    val drawerPanel get() = panels[drawerPanelId]?.panelView

    /**
     * GET a new ID for [drawerPanel].
     * The user cannot get or set the [drawerPanelId], so if the user [setPanel] with the same ID,
     * we need to get a new one for the [drawerPanel] that is not contained in the keys of
     * [panels] Map.
     */
    private val newDrawerPanelId: Int get() {
        val random = Random()
        var newId = 0
        while ( panels.keys.contains( newId ) ) { newId = random.nextInt() }
        return newId
    }

    /** The [Animator] for [topAppBar] */
    var toolbarAnimator: ViewPropertyAnimator? = null

    /**
     * Run [hideToolbar] and then [showToolbar].
     * @param delay the delay in millisec from the end of [hideToolbar] and the start of
     * [showToolbar]
     * @param doAfterHide the lambda to execute the [hideToolbar] the animation end.
     * @param doAfterShow the lambda to execute the [showToolbar] the animation end.
     */
    inline fun hideAndShowToolbar(
            delay: Long = 150,
            crossinline doAfterHide: () -> Unit = {},
            crossinline doAfterShow: () -> Unit = {}
    ) {
        hideToolbar { doAfterHide(); postDelayed( { showToolbar( doAfterShow ) }, delay ) }
    }

    /**
     * Start [ViewPropertyAnimator] that hide the [topAppBar]
     * @param doOnAnimationEnd the lambda to execute the the animation end.
     * @see [BottomAppBar.Behavior.slideDown] for duration and interpolator.
     */
    inline fun hideToolbar( crossinline doOnAnimationEnd: () -> Unit = {} ) {
        toolbarAnimator?.let {
            it.cancel()
            topAppBar?.clearAnimation()
        }
        topAppBar?.run {
            toolbarAnimator = animate().translationY( -height.toFloat() )
                    .setInterpolator( AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR )
                    .setDuration( 175 )
                    .withEndAction {
                        toolbarAnimator = null
                        doOnAnimationEnd()
                    }
        }
    }

    /**
     * Start [ViewPropertyAnimator] that show the [topAppBar]
     * @param doOnAnimationEnd the lambda to execute the the animation end.
     * @see [BottomAppBar.Behavior.slideUp] for duration and interpolator.
     */
    inline fun showToolbar( crossinline doOnAnimationEnd: () -> Unit = {} ) {
        toolbarAnimator?.let {
            it.cancel()
            topAppBar?.clearAnimation()
        }
        topAppBar?.run {
            toolbarAnimator = animate().translationY(0f )
                    .setInterpolator( AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR )
                    .setDuration( 225 )
                    .withEndAction {
                        toolbarAnimator = null
                        doOnAnimationEnd()
                    }
        }
    }

    /** The [Animator] */
    private var viewsAnimator: Animator? = null

    /* ===================================== I N I T ============================================ */

    init {

        doOnPreDraw {

            // Set navigation click listener to open the drawer, if any
            bottomAppBar?.setNavigationOnClickListener {
                drawer ?: return@setNavigationOnClickListener
                grabPanel( drawerPanelId )
                flyBar( Fly.MATCH_PANEL )
            }

            // Set Y and header's height for each panelView's
            panels.forEach { val panelView = it.value.panelView
                panelView?.y = height.toFloat()
                bottomAppBar?.height?.let { barHeight ->
                    panelView?.header?.layoutParams?.height = barHeight
                }
            }

            doOnNextLayout { setViewsY( bottomBarInitialY ) }
        }
    }

    /* =================================== P A N E L S ========================================== */

    /**
     * A lambda that will be invoked when a [MaterialPanel] changes its [Fly] state.
     * It will invoke [panelStateChangeListener] and, if needed, [panelOpenChangeListener] and
     * [panelCloseChangeListener]
     */
    private val panelStateChangeListenerInvoker: PanelRelativeStateChangeListener =
            { panelId, oldState, newState ->
                if ( oldState != newState ) {
                    panelStateChangeListener( panelId, newState )
                    if ( oldState == Fly.BOTTOM ) panelOpenChangeListener( panelId )
                    if ( newState == Fly.BOTTOM ) panelCloseChangeListener( panelId )
                }
            }

    /** A lambda that will be invoked when a [MaterialPanel] changes its [Fly] state */
    var panelStateChangeListener: PanelStateChangeListener = { _, _ -> }

    /**
     * A lambda that will be invoked when a [MaterialPanel] changes its [Fly] state from
     * [Fly.BOTTOM] to any other
     */
    internal var panelOpenChangeListener: PanelChangeListener = {}

    /**
     * A lambda that will be invoked when a [MaterialPanel] changes its [Fly] state to [Fly.BOTTOM]
     */
    internal var panelCloseChangeListener: PanelChangeListener = {}

    /** @see setPanel */
    @Deprecated("Use `setPanel` or `set` operator. This will be removed in the next " + // TODO remove in 1.2
            "release", ReplaceWith("setPanel") )
    fun addPanel( materialPanel: AbsMaterialPanel, id: Int ) {
        setPanel( id, materialPanel, false )
    }

    /** @see setPanel */
    fun setPanel( id: Int, materialPanel: AbsMaterialPanel ) {
        setPanel( id, materialPanel, false )
    }

    /**
     * Set a new [AbsMaterialPanel] to [panels], other panels with the same [id] will be removed.
     * @param materialPanel the [AbsMaterialPanel] to add.
     * @param id the ID of the [materialPanel].
     * @param isDrawer whether the [materialPanel] is the main drawer.
     */
    private fun setPanel( id: Int, materialPanel: AbsMaterialPanel, isDrawer: Boolean ) {
        // If the panel is not a drawer and the id is the same as drawerPanel and the drawer is
        // not null: Save the drawer in a temporary val, get a new ID for the drawer and
        // set the drawer in the new allocation into panels map.
        if ( ! isDrawer && id == drawerPanelId ) {
            panels[drawerPanelId]?.let { tempDrawer ->
                drawerPanelId = newDrawerPanelId
                panels[drawerPanelId] = tempDrawer
            }
        }

        // Remove the panel with the given ID.
        removePanel( id )

        // If is drawer, save the new drawerPanelId.
        if ( isDrawer ) drawerPanelId = id

        // Create a new PanelView of the given panel, add it to the layout, set its height to
        // WRAP_CONTENT and the y at the end of the layout (height). Then save the new PanelView
        // into the panel and save the panel in the panels map.
        val panelView = PanelView( this, materialPanel )
        addView( panelView )
        panelView.layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        panelView.y = height.toFloat()
        materialPanel.panelView = panelView
        panels[id] = materialPanel

        // Attach an observer to the panel.
        // If the Header or the Body changes, set them into the layout,
        // if the PanelView, re-set the panel.
        with( materialPanel ) {
            observe { newPanel, change -> when( change ) {
                AbsMaterialPanel.Change.HEADER ->       { setHeader( newPanel.header, panelView ) }
                AbsMaterialPanel.Change.BODY ->         { setBody(   newPanel.body,   panelView ) }
                AbsMaterialPanel.Change.PANEL_VIEW->    { setPanel( id, newPanel, isDrawer ) }
            } }

            // Set the Header and the Body into the layout.
            setHeader( header, panelView )
            setBody(   body,   panelView )
        }
    }

    /**
     * TODO
     * @param header
     * @param panelView
     */
    private fun setHeader( header: AbsMaterialPanel.IHeader?, panelView: PanelView ) {
        panelView.setHeader(this, header )
        ( header as? AbsMaterialPanel.BaseHeader<*> )?.let {
            header.applyIconTo( panelView.header.header_icon )

            header.applyTitleTo( panelView.header.header_title )
            header.titleColorHolder.applyToDrawable( panelView.header.header_close )

        } ?: ( header as? AbsMaterialPanel.CustomHeader )?.let {
            panelView.header = header.contentView
        }
    }

    /**
     * TODO
     * @param body
     * @param panelView
     */
    private fun setBody( body: AbsMaterialPanel.IBody?, panelView: PanelView ) {
        panelView.setBody( body )
        ( body as? AbsMaterialPanel.BaseBody<*> )?.let {
            if ( body.selectionColorHolder.resolveColor( context ) == null) {
                val color = draggingPanelHeaderColor ?: Color.GRAY
                body.selectionColor( color )
            }

            val bodyRecyclerView = panelView.body as RecyclerView
            bodyRecyclerView.layoutManager = LinearLayoutManager( context )
            val adapter = PanelBodyAdapter( body ) { closePanel() }
            body.addObserver { _, _ -> adapter.notifyDataSetChanged() }
            bodyRecyclerView.adapter = adapter

        } ?: ( body as? AbsMaterialPanel.CustomBody )?.let {
            panelView.body = body.contentView
        }
    }

    /**
     * Remove a [AbsMaterialPanel] from [panels] and its relative [AbsMaterialPanel.panelView] from
     * the layout.
     * @param id
     */
    fun removePanel( id: Int ) {
        panels[id]?.let {
            it.deleteObservers()
            removeView( it.panelView )
        }
        panels.remove( id )
    }

    /**
     * @see openPanel
     */
    fun openDrawer() { openPanel( drawerPanelId ) }

    /**
     * Open a [PanelView] with the given ID: [grabPanel] and [flyBar].
     * @param id the ID of the AbsMaterialPanel to open.
     */
    fun openPanel( id: Int ) {
        grabPanel( id )
        flyBar( Fly.MATCH_PANEL )
    }

    /** @see closePanel */
    fun closeDrawer() { closePanel() }

    /** Close the current open or grabbed [PanelView] */
    fun closePanel() { flyBar( Fly.BOTTOM ) }

    /**
     * Grab a [PanelView] to be dragged by the user.
     * @param id the ID of the [AbsMaterialPanel] which [AbsMaterialPanel.panelView] needs to be
     * grabbed.
     */
    private fun grabPanel( id: Int ) {
        draggingPanelId = id
        val draggingPanel = panels[id]

        // Store the PanelView.
        draggingPanelView = draggingPanel?.panelView

        // Store the color of the header of the PanelView.
        draggingPanelHeaderColor =
                ( draggingPanel?.header as? AbsMaterialPanel.BaseHeader<*> )
                        ?.backgroundColorHolder?.resolveColor( context )

                ?: ( ( draggingPanel?.header as? AbsMaterialPanel.CustomHeader )
                        ?.contentView?.background as? ColorDrawable )?.color
    }

    /* ================================= D R A G G I N G ======================================== */

    /** The [MotionEvent.getY] of the last [MotionEvent.ACTION_DOWN] */
    private var downY = 0f

    /**
     * The [bottomAppBar] [MaterialBottomAppBar.getY] in the moment of the last
     * [MotionEvent.ACTION_DOWN].
     */
    private var bottomBarDownY = 0f

    /** Whether the [bottomAppBar] is being dragged */
    private var draggingBar = false

    /** The timestamp of the last [MotionEvent.ACTION_DOWN] */
    private var downEventTimestamp = 0L // TODO currently updated but not used

    /**
     * The timestamp of the last [MotionEvent.ACTION_DOWN] consumed by the dragging of
     * [bottomAppBar].
     */
    private var consumedEventTimestamp = 0L // TODO currently updated but not used

    /**
     * @return whether the [MotionEvent] should be intercepted
     * `true` of if is [MotionEvent.ACTION_UP] or [MotionEvent.ACTION_CANCEL] and Y between
     * [downY] and [MotionEvent.getY] exceed [MIN_INTERCEPT_DRAG_THRESHOLD]
     */
    override fun onInterceptTouchEvent( event: MotionEvent ): Boolean {
        val actionDown = event.action == MotionEvent.ACTION_DOWN
        val actionUp = event.action == MotionEvent.ACTION_UP ||
                event.action == MotionEvent.ACTION_CANCEL

        // Is this event.y or last event.y in bottomBarRange
        val inRange = downY in bottomBarRange || event.y in bottomBarRange

        // If actionDown update downY and downEventTimestamp
        if ( actionDown ) {
            downY = event.y
            downEventTimestamp = System.currentTimeMillis()
        }

        // Get direction and check if draggingPanelRecyclerView should scroll
        val direction = downY.compareTo( event.y )
        val shouldScrollDrawerRecyclerView =
                event.action == MotionEvent.ACTION_MOVE && bottomAppBar!!.y < 1 &&
                        draggingPanelRecyclerView?.canScrollVertically( direction ) ?: false

        // If draggingPanelRecyclerView should NOT scroll and this event.y or last event.y is in
        // bottomBarRange, call onTouchEvent
        if ( ! shouldScrollDrawerRecyclerView && inRange ) {
            if ( onTouchEvent( event ) )
                consumedEventTimestamp = System.currentTimeMillis()

        // If this event.y or last event.y is NOT in bottomBarRange, close the panel
        } else if ( ! inRange )
            flyBar( Fly.BOTTOM )

        // If is actionUp, INTERCEPT if and movement exceed the MIN_INTERCEPT_DRAG_THRESHOLD
        if ( actionUp ) {
            val moved = Math.abs( downY - event.y ) > MIN_INTERCEPT_DRAG_THRESHOLD

            downY = 0f
            return moved
        }

        // Don't INTERCEPT by default
        return false
    }

    /** @return whether an event has been consumed */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent( event: MotionEvent ): Boolean {

        // Get direction and check if draggingPanelRecyclerView should scroll
        val direction = downY.compareTo( event.y )
        val shouldScrollDrawerRecyclerView =
                event.action == MotionEvent.ACTION_MOVE && bottomAppBar!!.y < 1 &&
                        draggingPanelRecyclerView?.canScrollVertically( direction ) ?: false

        // If draggingPanelRecyclerView should scroll, return `false` and don't consume any other
        // event
        if ( shouldScrollDrawerRecyclerView ) return false

        // Consume an event regarding the current action.
        return when( event.action ) {
            MotionEvent.ACTION_DOWN -> onDown( event )
            MotionEvent.ACTION_MOVE -> onMove( event )
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> releaseBar( event )
            else -> super.onTouchEvent( event )
        }
    }

    /** If there is a [drawer], grab [MaterialBottomAppBar] for move */
    private fun grabBar() {
        // return if no drawer available
        drawer ?: return

        // If bar is in initial state, store if fab is visible and grab drawer's panel
        if ( isBarInInitialState ) {
            hasFab = fab?.isVisible == true
            grabPanel( drawerPanelId )
        }
        // Store that is dragging bar
        draggingBar = true
    }

    /**
     * Release the [MaterialBottomAppBar]. Opposite as [grabBar].
     *
     * @param event the current [MotionEvent] for define the behaviour of the [bottomAppBar]. Where
     * should it [flyBar]
     *
     * @return `true` if any event is consumed ( basically if [bottomAppBar] is NOT `null` )
     */
    private fun releaseBar( event: MotionEvent ): Boolean {
        if ( bottomAppBar == null ) return false

        if ( draggingBar ) {
            // Store that is NOT dragging bar
            draggingBar = false

            // Check if a minimum movement has been made for fly the bar to the dragging direction
            // of let it fly back fto its old position
            val inThreshold = Math.abs( event.y - downY ) > MIN_FLY_DRAG_THRESHOLD

            // If inThreshold fly the bar in the dragging direction
            if ( inThreshold ) {
                val isDraggingUp = event.y < downY

                val fly = if ( ! isDraggingUp ) Fly.BOTTOM
                else if ( bottomAppBar!!.y < matchPanelY ) Fly.TOP
                else Fly.MATCH_PANEL

                flyBar( fly )

            // If NOT inThreshold fly the bar to its old position
            } else flyBar( lastFly )
        }
        // Return that the event has been consumed
        return true
    }

    /**
     * Drag the [bottomAppBar] withing [MotionEvent].
     * Limits are `0f` ( the top of the [MaterialBottomDrawerLayout] ) and [bottomBarInitialY]
     *
     * @see setViewsY
     */
    private fun dragBar( y: Float ) = bottomAppBar?.let {
        val toY = ( bottomBarDownY - ( downY - y ) )
                .coerceAtLeast( 0f )
                .coerceAtMost( bottomBarInitialY )
        setViewsY( toY )
    }

    /**
     * Handle [MotionEvent.ACTION_DOWN]. If [MotionEvent.getY] is in [bottomBarRange], store
     * [MaterialBottomAppBar.getY]
     *
     * @return `true` if [MotionEvent.getY] is in [bottomBarRange]
     */
    private fun onDown( event: MotionEvent ) = if ( event.y in bottomBarRange ) {
        bottomBarDownY = bottomAppBar?.y ?: 0f
        true
    } else false

    /**
     * Handle [MotionEvent.ACTION_MOVE].
     * @return:
     * * If is [draggingBar], [dragBar] to [MotionEvent.getY] and return `true`
     * * else if [downY] is in [bottomBarRange], [grabBar] and return `true`
     * * else return `false`
     */
    private fun onMove( event: MotionEvent ) = when {
        draggingBar -> { dragBar( event.y ); true }
        downY in bottomBarRange -> { grabBar(); true }
        else -> false
    }

    /** Enum for the possible directions of [flyBar] */
    enum class Fly { TOP, BOTTOM, MATCH_PANEL }

    /** Keep track of the direction of the last [flyBar] */
    private var lastFly = Fly.BOTTOM

    /**
     * Fly [bottomAppBar], if not null, to the requested [Fly] direction
     * @see animateViewsY
     */
    private fun flyBar( fly: Fly ) {
        bottomAppBar ?: return

        // Call panelStateChangeListenerInvoker
        panelStateChangeListenerInvoker( draggingPanelId!!, lastFly, fly )

        // Store the requested Fly as lastFly
        lastFly = fly

        val toY = when( fly ) {
            Fly.TOP ->          0f
            Fly.BOTTOM ->       bottomBarInitialY
            Fly.MATCH_PANEL ->  matchPanelY
        }
        animateViewsY( toY )
    }

    /** Whether [fab] is visible on [bottomBarInitialY] */
    private var hasFab = fab?.isVisible

    /** Animate [bottomAppBar] and [draggingPanelView] to the requested [toY] */
    private fun animateViewsY( toY: Float ) {
        // Cancel the current viewsAnimator, if any
        viewsAnimator?.cancel()

        // Get the starting point of bottomAppBar.y
        val fromY = bottomAppBar!!.y
        if ( fromY == bottomBarInitialY ) hasFab = fab?.isVisible == true
        // Calculate the duration of the animation
        val animationDuration = Math.abs( fromY - toY ) / 2

        // Create the animation, start it and set to viewsAnimator. On end set viewsAnimator to null
        viewsAnimator = ValueAnimator.ofFloat( fromY, toY ).apply {
            duration = animationDuration.toLong()

            addUpdateListener { setViewsY( it.animatedValue as Float ) }

            doOnEnd { viewsAnimator = null }
            start()
        }
    }

    /**
     * Set [bottomAppBar] and [draggingPanelView] to the requested [y] and apply the needed params
     * ( color, alpha, shape, etc )
     *
     * Do nothing if [y] is less than 0f, since we don't want the [View]s to go above the current
     * [MaterialBottomDrawerLayout]
     */
    private fun setViewsY( y: Float ) {
        if ( y < 0f ) return

        // Create a non-null reference to bottomAppBar
        val bottomAppBar = bottomAppBar ?: throw AssertionError()

        // Set views' y
        bottomAppBar.y = y
        draggingPanelView?.y = y

        // Store a reference to the available height where the views can be moved, it's represented
        // by the layout's height minus the bottomAppBar's height
        val availableHeight = height - bottomAppBar.height

        // A percentage representing the position of Y relatively to the space that goes from the
        // top of this layout ( 0 ) to the point where the bottomAppBar would be on Fly.MATCH_PANEL
        // state ( matchPanelY )
        val topPercentage = 1f / ( matchPanelY / y.coerceAtMost( matchPanelY ) )

        // A percentage representing the position of Y relatively to the space that goes from point
        // where the bottomAppBar would be on Fly.MATCH_PANEL state ( matchPanelY ) the bottom
        // ( availableHeight )
        val bottomPercentage =  1f / ( ( availableHeight - matchPanelY ) / ( y - matchPanelY )
                .coerceAtLeast( 0f ) )

        // Set bottomAppBar's cornersInterpolation according to topPercentage
        bottomAppBar.cornersInterpolation = topPercentage

        // Blend the bottomAppBar's color according to bottomPercentage
        bottomBarInitialColor?.let { blendFrom ->
            val blendTo = draggingPanelHeaderColor ?: blendFrom
            val blend = ColorUtils.blendARGB(
                    blendFrom, blendTo, 1f - bottomPercentage
            )
            ColorHolder( color = blend ).applyToBackground( bottomAppBar )
        }

        // If bottomAppBar is in its initial position, set all the panels' Y to availableHeight,
        // scroll draggingPanelRecyclerView to 0 and set the bottomAppBar's menu visible.
        // Else just hide the bottomAppBar's menu
        if ( isBarInInitialState ) {
            panels.forEach { it.value.panelView?.y = availableHeight.toFloat() }
            draggingPanelRecyclerView?.scrollToPosition( 0 )

            bottomAppBar.menu.setGroupVisible( 0, true )

        } else {
            bottomAppBar.menu.setGroupVisible( 0, false )
        }

        // Set bottomAppBar's children alpha and enabled state
        bottomAppBar.children.forEach {
            it.alpha = bottomPercentage
            it.isClickable =   isBarInInitialState
            it.isEnabled =     isBarInInitialState
        }
        // Set draggingPanelView's children alpha and enabled state
        draggingPanelView?.fadeHeader( 1f - bottomPercentage, ! isBarInInitialState )

        hasFab ?: return
        // Show fab if isBarInInitialState and hasFab, else hide it
        fab?.show( isBarInInitialState && hasFab!! )
    }


    /** EXPERIMENTAL: Set true for auto-hide [bottomAppBar] when a soft keyboard is open / visible */
    var hideBottomAppBarOnSoftKeyboard = true

    /** The [Rect] that will receive the [getWindowVisibleDisplayFrame] */
    private val rect = Rect()

    /** Whether the soft keyboard is currently open / visible */
    private var isSoftKeyboardOpen = false

    /**
     * [MaterialBottomAppBar.show] or [MaterialBottomAppBar.hide] whether if the soft keyboard is
     * show or not.
     * We will compare the display height to the visible frame height for understand if the
     * soft keyboard is shown.
     */
    override fun onLayout( changed: Boolean, l: Int, t: Int, r: Int, b: Int ) {
        super.onLayout( changed, l, t, r, b )
        if ( ! hideBottomAppBarOnSoftKeyboard ) return

        getWindowVisibleDisplayFrame( rect )

        val wasSoftKeyboardOpen = isSoftKeyboardOpen
        isSoftKeyboardOpen = kotlin.run {
            val heightDiff = rootView.height - ( rect.bottom - rect.top )
            heightDiff > 500
        }

        if ( isSoftKeyboardOpen != wasSoftKeyboardOpen )
            onSoftKeyboardStateChange( isSoftKeyboardOpen )
    }

    /**
     * Callback executed when [isSoftKeyboardOpen] changes.
     * @param open whether the soft keyboard is open / visible.
     */
    private fun onSoftKeyboardStateChange( open: Boolean ) {
        bottomAppBar?.let {
            if ( open ) it.hide( true )
            else {
                setViewsY( bottomBarInitialY )
                it.show()
            }
        }
    }
}

/** Typealias for a lambda that receives a [MaterialPanel]s id */
typealias PanelChangeListener = ( panelId: Int ) -> Unit

/** Typealias for a lambda that receives a [MaterialPanel]s id [Int] and a [Fly] state */
typealias PanelStateChangeListener = ( panelId: Int, state: Fly ) -> Unit

/**
 * Typealias for a lambda that receives a [MaterialPanel]s id [Int], the old [Fly] state and the
 * new [Fly] state */
private typealias PanelRelativeStateChangeListener =
        ( panelId: Int, oldState: Fly, newState: Fly ) -> Unit