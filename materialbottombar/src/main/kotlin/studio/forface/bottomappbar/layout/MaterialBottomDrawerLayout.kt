@file:Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName", "unused")

package studio.forface.bottomappbar.layout

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
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import androidx.core.graphics.ColorUtils
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomappbar.BottomAppBar
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
import java.util.*
import android.opengl.ETC1.getHeight



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

    /**
     * GET whether the [bottomAppBar] is at its initial state ( [Gravity.BOTTOM] ).
     */
    private val isBarInInitialState get() = bottomAppBar?.y == bottomBarInitialY

    /**
     * A [MutableMap] composed by the Panel ID and the [MaterialPanel].
     */
    var panels = mutableMapOf<Int, MaterialPanel>()

    /**
     * The [PanelView] that is currently being dragged by the user.
     */
    private var draggingPanelView: PanelView? = null

    /**
     * It represents the Background Color of the current dragging Panel.
     * If no Panel is being dragged it is null.
     * If the [MaterialPanel.header] is [MaterialPanel.AbsHeader], it will be
     * [ColorHolder.resolveColor];
     * If the [MaterialPanel.header] is [MaterialPanel.CustomHeader] and the [View.getBackground]
     * [MaterialPanel.CustomHeader.contentView] is [ColorDrawable], it will be
     * [ColorDrawable.getColor];
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
     * GET a [RecyclerView] if the [draggingPanelView] [MaterialPanel.body] IS or CONTAINS a
     * [RecyclerView].
     * We need it for understand if a scroll event should move the [draggingPanelView] or
     * scroll the [RecyclerView].
     */
    private val draggingPanelRecyclerView: RecyclerView? get() =
        draggingPanelView?.body as? RecyclerView
                ?: ( draggingPanelView?.body as? ViewGroup )?.findChildType()

    /**
     * The Drawer Menu.
     */
    var drawer: MaterialDrawer?
        get() = panels[drawerPanelId] as MaterialDrawer?
        set( value ) = value.run {
            value?.let { addPanel( it, drawerPanelId,true ) }
        }

    /**
     * The ID of the [drawerPanel]
     */
    private var drawerPanelId = 0

    /**
     * The [MaterialPanel] which represent the [drawer] [MaterialDrawer.panelView].
     */
    val drawerPanel get() = panels[drawerPanelId]?.panelView

    /**
     * GET a new ID for [drawerPanel].
     * The user cannot get or set the [drawerPanelId], so if the user [addPanel] with the same ID,
     * we need to get a new one for the [drawerPanel] that is not contained in the keys of
     * [panels] Map.
     */
    private val newDrawerPanelId: Int get() {
        val random = Random()
        var newId = 0
        while ( panels.keys.contains( newId ) ) { newId = random.nextInt() }
        return newId
    }

    /**
     * The [Animator] for [topAppBar].
     */
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
                    .doOnEnd {
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
                    .doOnEnd {
                        toolbarAnimator = null
                        doOnAnimationEnd()
                    }
        }
    }

    /**
     * The [Animator].
     */
    private var viewsAnimator: Animator? = null

    /* ========================================================================================== */

    init {

        doOnPreDraw {
            bottomAppBar?.setNavigationOnClickListener {
                drawer ?: return@setNavigationOnClickListener
                grabPanel( drawerPanelId )
                flyBar( Fly.MATCH_PANEL )
            }

            panels.forEach { val panelView = it.value.panelView
                panelView?.y = height.toFloat()
                bottomAppBar?.height?.let { barHeight ->
                    panelView?.header?.layoutParams?.height = barHeight
                }
            }

            doOnNextLayout { setViewsY( bottomBarInitialY ) }
        }
    }

    /* ========================================================================================== */

    /**
     * @see addPanel
     */
    fun addPanel( materialPanel: MaterialPanel, id: Int ) {
        addPanel( materialPanel, id,false )
    }

    /**
     * Adds a new [MaterialPanel] to [panels].
     * @param materialPanel the [MaterialPanel] to add.
     * @param id the ID of the [materialPanel].
     * @param isDrawer whether the [materialPanel] is the main drawer.
     */
    private fun addPanel( materialPanel: MaterialPanel, id: Int, isDrawer: Boolean ) {
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
        if ( isDrawer ) { drawerPanelId = id }

        // Create a new PanelView of the given panel, add it to the layout, set its height to
        // WRAP_CONTENT and the y at the end of the layout (height). Then save the new PanelView
        // into the panel and save the panel in the panels map.
        val panelView = PanelView(this, materialPanel)
        addView( panelView )
        panelView.layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        panelView.y = height.toFloat()
        materialPanel.panelView = panelView
        panels[id] = materialPanel

        // Attach an observer to the panel.
        // If the Header or the Body changes, set them into the layout,
        // if the PanelView, re-set the panel.
        materialPanel.run {
            observe { newPanel, change -> when( change ) {
                is MaterialPanel.Change.HEADER ->       { setHeader( newPanel.header, panelView ) }
                is MaterialPanel.Change.BODY ->         { setBody(   newPanel.body,   panelView ) }
                is MaterialPanel.Change.PANEL_VIEW->    { addPanel(  newPanel, id, isDrawer     ) }
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

    /**
     * TODO
     * @param body
     * @param panelView
     */
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

    /**
     * Remove a [MaterialPanel] from [panels] and its relative [MaterialPanel.panelView] from
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
     * @param id the ID of the MaterialPanel to open.
     */
    fun openPanel( id: Int ) {
        grabPanel( id )
        flyBar( Fly.MATCH_PANEL )
    }

    /**
     * @see closePanel
     */
    fun closeDrawer() { closePanel() }

    /**
     * Close the current open or grabbed [PanelView].
     */
    fun closePanel() {
        flyBar( Fly.BOTTOM )
    }

    /**
     * Grab a [PanelView] to be dragged by the user.
     * @param id the ID of the [MaterialPanel] which [MaterialPanel.panelView] needs to be
     * grabbed.
     */
    private fun grabPanel( id: Int ) {
        val draggingPanel = panels[id]

        // Store the PanelView.
        draggingPanelView = draggingPanel?.panelView

        // Store the color of the header of the PanelView.
        draggingPanelHeaderColor =
                ( draggingPanel?.header as? MaterialPanel.AbsHeader<*> )
                        ?.backgroundColorHolder?.resolveColor( context )

                ?: ( ( draggingPanel?.header as? MaterialPanel.CustomHeader )
                        ?.contentView?.background as? ColorDrawable )?.color
    }

    /* ========================================================================================== */

    /**
     * The [Rect] that will receive the [getWindowVisibleDisplayFrame].
     */
    val rect = Rect()

    /**
     * Here we will [MaterialBottomAppBar.show] or [MaterialBottomAppBar.hide] whether if the
     * soft keyboard is show or not.
     * We will compare the display height to the visible frame height for understand if the
     * soft keyboard is shown.
     */
    override fun onLayout( changed: Boolean, l: Int, t: Int, r: Int, b: Int ) {
        super.onLayout( changed, l, t, r, b )

        getWindowVisibleDisplayFrame( rect )

        bottomAppBar?.let {
            val heightDiff = rootView.height - ( rect.bottom - rect.top )
            if ( heightDiff > 500 ) it.hide() else it.show()
        }
    }

    /**
     * The [MotionEvent.getY] of the last [MotionEvent.ACTION_DOWN].
     */
    private var downY = 0f

    /**
     * The [bottomAppBar] [MaterialBottomAppBar.getY] in the moment of the last
     * [MotionEvent.ACTION_DOWN].
     */
    private var bottomBarDownY = 0f

    /**
     * Whether the [bottomAppBar] is being dragged.
     */
    private var draggingBar = false

    /**
     * The timestamp of the last [MotionEvent.ACTION_DOWN].
     */
    private var downEventTimestamp = 0L

    /**
     * The timestamp of the last [MotionEvent.ACTION_DOWN] consumed by the dragging of
     * [bottomAppBar].
     */
    private var consumedEventTimestamp = 0L

    override fun onInterceptTouchEvent( event: MotionEvent ): Boolean {
        val actionDown = event.action == MotionEvent.ACTION_DOWN
        val actionUp = event.action == MotionEvent.ACTION_UP ||
                event.action == MotionEvent.ACTION_CANCEL

        val inRange = downY in bottomBarRange || event.y in bottomBarRange

        if ( actionDown ) {
            downY = event.y
            downEventTimestamp = System.currentTimeMillis()
        }

        val direction = downY.compareTo( event.y )
        val shouldScrollDrawerRecyclerView =
                event.action == MotionEvent.ACTION_MOVE && bottomAppBar!!.y < 1 &&
                        draggingPanelRecyclerView?.canScrollVertically( direction ) ?: false

        if ( ! shouldScrollDrawerRecyclerView && inRange ) {
            if ( onTouchEvent( event ) )
                consumedEventTimestamp = System.currentTimeMillis()

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

    private fun grabBar() {
        drawer ?: return
        if ( isBarInInitialState ) {
            hasFab = fab?.isVisible == true
            grabPanel(drawerPanelId)
        }
        draggingBar = true
    }
    private fun releaseBar( event: MotionEvent ) = bottomAppBar?.let {
        if ( draggingBar ) {
            draggingBar = false

            val inThreshold = Math.abs( event.y - downY ) > MIN_FLY_DRAG_THRESHOLD

            if (inThreshold) {
                val isDraggingUp = event.y < downY

                val fly = if ( ! isDraggingUp ) Fly.BOTTOM
                else if ( it.y < matchPanelY ) Fly.TOP
                else Fly.MATCH_PANEL

                flyBar( fly )

            } else flyBar( lastFly )

            true

        } else true

    } ?: false

    private fun dragBar( y: Float ) = bottomAppBar?.let {
        val toY = ( bottomBarDownY - ( downY - y ) )
                .coerceAtLeast(0f )
                .coerceAtMost( bottomBarInitialY )
        setViewsY( toY )
    }

    private fun onDown( event: MotionEvent ) = if ( event.y in bottomBarRange ) {
        bottomBarDownY = bottomAppBar?.y ?: 0f
        true
    } else false


    private fun onMove( event: MotionEvent ) = when {
        draggingBar -> { dragBar( event.y ); true }
        downY in bottomBarRange -> { grabBar(); true }
        else -> false
    }

    enum class Fly { TOP, BOTTOM, MATCH_PANEL }
    private var lastFly = Fly.BOTTOM
    private fun flyBar( fly: Fly ) {
        lastFly = fly
        bottomAppBar?.let {
            val toY = when( fly ) {
                Fly.TOP ->          0f
                Fly.BOTTOM ->       bottomBarInitialY
                Fly.MATCH_PANEL -> matchPanelY
            }
            animateViewsY( toY )
        }
    }

    private var hasFab = fab?.isVisible
    private fun animateViewsY( toY: Float ) {
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

    @SuppressLint("ResourceType")
    private fun setViewsY( y: Float ) {
        if ( y < 0f ) return

        bottomAppBar!!.y =  y
        draggingPanelView?.y =      y

        val height = height - bottomAppBar!!.height

        //val totalPercentage =   1f / ( height / y )
        val topPercentage =     1f / ( matchPanelY / y.coerceAtMost( matchPanelY ) )
        val bottomPercentage =  1f / ( ( height - matchPanelY ) / ( y - matchPanelY ).coerceAtLeast(0f ) )

        bottomAppBar!!.cornersInterpolation =   topPercentage

        bottomBarInitialColor?.let { blendFrom ->
            val blendTo = draggingPanelHeaderColor ?: blendFrom
            val blend = ColorUtils.blendARGB(
                    blendFrom, blendTo, 1f - bottomPercentage
            )
            ColorHolder( color = blend ).applyToBackground( bottomAppBar!! )
        }

        if ( isBarInInitialState ) {
            panels.forEach { it.value.panelView?.y = height.toFloat() }
            draggingPanelRecyclerView?.scrollToPosition( 0 )

            bottomAppBar!!.menu.setGroupVisible( 0, true )
        } else {
            bottomAppBar!!.menu.setGroupVisible( 0, false )
        }

        //drawerHeaderColorHolder?.applyToBackground( bottomAppBar!! )
        bottomAppBar!!.children.forEach {
            it.alpha = bottomPercentage
            it.isClickable =   isBarInInitialState
            it.isEnabled =     isBarInInitialState
        }
        draggingPanelView?.fadeHeader(1f - bottomPercentage, ! isBarInInitialState )

        hasFab ?: return
        fab?.show(isBarInInitialState && hasFab!! )
    }

}