@file:Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName")

package studio.forface.bottomappbar.materialbottomdrawer

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import androidx.core.graphics.ColorUtils
import androidx.core.view.children
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapePathModel
import kotlinx.android.synthetic.main.drawer_header.view.*
import studio.forface.bottomappbar.materialbottomappbar.MaterialBottomAppBar
import studio.forface.bottomappbar.materialbottomdrawer.drawer.MaterialDrawer
import studio.forface.bottomappbar.materialbottomdrawer.holder.ColorHolder
import studio.forface.bottomappbar.utils.elevationCompat
import studio.forface.bottomappbar.utils.findChild
import studio.forface.materialbottombar.bottomappbar.R

class MaterialBottomDrawerLayout @JvmOverloads constructor (
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CoordinatorLayout( context, attrs, defStyleAttr ) {

    private val DRAG_THRESHOLD by lazy { height / 10 }

    private val BOTTOM_BAR_RANGE get() =
        bottomAppBar?.let {
            it.y .. height.toFloat()
        } ?: 0f .. 0f

    val bottomAppBar    get() = findChild<MaterialBottomAppBar>()
    val fab             get() = findChild<FloatingActionButton>()
    val topAppBar       get() = findChild<AppBarLayout>()

    private val bottomAppBarInitialY by lazy { bottomAppBar?.y ?: height.toFloat() }
    private val matchDrawerY get() =
        height - ( drawerHeader.height + drawerBody.height ).toFloat()

    private var viewsAnimator: Animator? = null
    private var drawerHeaderColorHolder: ColorHolder? = null

    var drawer: MaterialDrawer = MaterialDrawer()
        set( value ) = value.run {
            field = this

            header?.let {
                it.icon?.applyTo( drawerHeader.header_icon )
                it.title?.run {
                    applyTo( drawerHeader.header_title )
                    colorHolder.applyToDrawable( drawerHeader.header_close )
                }
                drawerHeaderColorHolder = it.backgroundColor?.colorHolder
                null
            }
        }

    private val drawerHeader: ConstraintLayout by lazy {
        LayoutInflater.from( context )
                .inflate( R.layout.drawer_header,this, false )
                .apply {
                    header_shadow.elevationCompat = 14f
                    elevationCompat = 8f
                    header_close.setOnClickListener { flyBar( Fly.BOTTOM ) }
                } as ConstraintLayout
    }
    private val drawerBody      by lazy {
        LinearLayout( context, attrs, defStyleAttr ).apply {
            fun textView( id: Int ) = TextView( context, attrs, defStyleAttr ).apply {
                this.id = id
                text = "I'm a menu item :)"
                textSize = 30f
                setPadding( 40, 20, 0, 0 )

            }

            setBackgroundColor( Color.WHITE )
            orientation = LinearLayout.VERTICAL
            for ( i in 0 until 10 ) addView( textView( i ) )
        }
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

                addView( drawerBody )
                drawerBody.layoutParams.width = CoordinatorLayout.LayoutParams.MATCH_PARENT

                addView( drawerBottomBackground )
                drawerBottomBackground.layoutParams.height = CoordinatorLayout.LayoutParams.MATCH_PARENT

                doOnNextLayout { setViewsY( bottomAppBarInitialY ) }
            }
        }
    }

    private var downY = 0f
    private var bottomBarDownY = 0f
    private var draggingBar = false
    override fun onInterceptTouchEvent( event: MotionEvent ): Boolean {

        fun grabBar() = kotlin.run {
            draggingBar = true
            false
        }
        fun releaseBar() = bottomAppBar?.let {
            draggingBar = false

            val inThreshold = Math.abs( event.y - downY ) > DRAG_THRESHOLD

            if ( inThreshold ) {
                val isDraggingUp = event.y < downY

                val fly = if ( ! isDraggingUp ) Fly.BOTTOM
                else if (event.y < matchDrawerY) Fly.TOP
                else Fly.MATCH_DRAWER

                flyBar(fly)

            } else flyBar( lastFly )

            false
        } ?: false

        fun dragBar( y: Float ) = bottomAppBar?.let {
            val toY = Math.abs( bottomBarDownY - ( downY - y ) )
            setViewsY( toY )
            false
        } ?: false


        fun onDown() = when {
            event.y in BOTTOM_BAR_RANGE -> {
                downY = event.y
                bottomBarDownY = bottomAppBar?.y ?: 0f
                false
            }
            else -> false
        }

        fun onMove() = when {
            draggingBar -> dragBar( event.y )
            downY in BOTTOM_BAR_RANGE -> grabBar()
            else -> false
        }

        return when( event.action ) {
            MotionEvent.ACTION_DOWN -> onDown()
            MotionEvent.ACTION_MOVE -> onMove()
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> releaseBar()
            else -> super.onInterceptTouchEvent( event )
        }
    }

    enum class Fly { TOP, BOTTOM, MATCH_DRAWER }
    private var lastFly = Fly.BOTTOM
    private fun flyBar( fly: Fly ) {
        lastFly = fly
        bottomAppBar?.let {
            val toY = when( fly ) {
                Fly.TOP ->          0f
                Fly.BOTTOM ->       bottomAppBarInitialY
                Fly.MATCH_DRAWER -> height - ( drawerHeader.height + drawerBody.height ).toFloat()
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
        bottomAppBar!!.y =  y
        drawerHeader.y =    y
        drawerBody.y =      y + bottomAppBar!!.height
        drawerBottomBackground.y = drawerBody.y + drawerBody.height

        val height = height - bottomAppBar!!.height

        val totalPercentage =   1f / ( height / y )
        val topPercentage =     1f / ( matchDrawerY / y.coerceAtMost( matchDrawerY ) )
        val bottomPercentage =  1f / ( ( height - matchDrawerY ) / ( y - matchDrawerY ).coerceAtLeast(0f ) )

        bottomAppBar!!.cornersInterpolation =   topPercentage

        val isInitialState = y == bottomAppBarInitialY

        if ( bottomAppBarInitialColor == null ) bottomAppBarInitialColor =
                ( bottomAppBar?.background as? MaterialShapeDrawable )?.tintList?.defaultColor

        bottomAppBarInitialColor?.let { blendFrom ->
            val blendTo = drawerHeaderColorHolder?.resolveColor( context ) ?: blendFrom
            val blend = ColorUtils.blendARGB(
                    blendFrom, blendTo, 1f - bottomPercentage
            )
            ColorHolder( color = blend ).applyToBackground( bottomAppBar!! )
        }

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

    private fun FloatingActionButton.show( show: Boolean ) {
        if      (   show && ! isOrWillBeShown  ) show()
        else if ( ! show && ! isOrWillBeHidden ) hide()
    }

}

