@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package studio.forface.bottomappbar.materialbottomappbar

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.*
import studio.forface.bottomappbar.utils.findChild
import studio.forface.bottomappbar.utils.getField
import studio.forface.bottomappbar.utils.reflection
import studio.forface.bottomappbar.utils.setField
import studio.forface.materialbottombar.bottomappbar.R


class MaterialBottomAppBar @JvmOverloads constructor (
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomAppBar( context, attrs, defStyleAttr ) {

    val fab: FloatingActionButton? get() = ( parent as? ViewGroup )?.findChild()

    var hideBarOnScroll: Boolean = false
    var hideFabOnScroll: Boolean

    @Deprecated("use hideBarOnScroll", ReplaceWith("hideBarOnScroll"))
    override fun getHideOnScroll(): Boolean {
        return super.getHideOnScroll()
    }
    @Deprecated("use hideBarOnScroll", ReplaceWith("hideBarOnScroll"))
    override fun setHideOnScroll( hide: Boolean ) {
        super.setHideOnScroll( hide )
    }

    private var _leftCornerRadius: Int
    private var _leftCornerStyle: Int
    private var _rightCornerRadius: Int
    private var _rightCornerStyle: Int

    private val navView: ImageButton by reflection( "mNavButtonView",2 )
    private var modeAnimator: Animator? by reflection()

    internal lateinit var leftCorner: StaticCornerTreatment
    internal lateinit var rightCorner: StaticCornerTreatment


    fun setCornersRadius( left: Int, right: Int ) {
        _leftCornerRadius = left
        _rightCornerRadius = right
        drawBackgroundTopCorners()
    }

    internal var menuIconAlpha: Float
        get() = navView.alpha
        set( value ) { navView.alpha = value }

    fun unFlatCorners() { flatCorners( false ) }

    fun flatCorners( flat: Boolean = true ) {
        val ( from, to ) = if ( flat ) 1f to 0f else 0f to 1f
        val animator = ValueAnimator.ofFloat( from, to )

        animator.addUpdateListener { animation ->
            cornersInterpolation = animation.animatedValue as Float
        }
        animator.duration = 300L

        val set = AnimatorSet()
        set.playTogether( animator )

        modeAnimator = set
        modeAnimator!!.doOnEnd { modeAnimator = null }
        modeAnimator!!.start()
        isFabAttached = isFabAttached
    }

    var cornersInterpolation: Float
        get() = leftCorner.fixedInterpolation
        set( value ) {
            leftCorner.fixedInterpolation = value
            rightCorner.fixedInterpolation = value
            isFabAttached = isFabAttached
        }

    var backgroundAlpha: Int
        get() = DrawableCompat.getAlpha( background )
        set( value ) {
            background.alpha = value
            isFabAttached = isFabAttached
        }

    init {
        fixNavigationIconPadding()
        doOnPreDraw {
            (layoutParams as CoordinatorLayout.LayoutParams).apply {
                behavior = Behavior( context, attrs )
                hideBarOnScroll = hideOnScroll
                hideOnScroll = true
            }
        }

        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MaterialBottomAppBar,
                0, 0
        )

        try {
            hideFabOnScroll = a.getBoolean(
                    R.styleable.MaterialBottomAppBar_hideFabOnScroll, false )

            _leftCornerRadius = a.getDimensionPixelSize(
                    R.styleable.MaterialBottomAppBar_leftCornerRadius,0 )
            _leftCornerStyle = a.getInt(
                    R.styleable.MaterialBottomAppBar_leftCornerStyle, 0 )

            _rightCornerRadius = a.getDimensionPixelSize(
                    R.styleable.MaterialBottomAppBar_rightCornerRadius,0 )
            _rightCornerStyle = a.getInt(
                    R.styleable.MaterialBottomAppBar_rightCornerStyle,0 )

        } finally {
            a.recycle()
        }

        doOnLayout { drawBackgroundTopCorners() }
    }

    private fun fixNavigationIconPadding() {
        val px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,16f, resources.displayMetrics ).toInt()
        navView.setPadding( px, px, px, px )
    }

    private fun drawBackgroundTopCorners() {
        val leftCornerRadius  = _leftCornerRadius.toFloat()
        val rightCornerRadius = _rightCornerRadius.toFloat()

        val shapeDrawable = ( background as MaterialShapeDrawable )
        val shapePathModel = shapeDrawable.shapedViewModel!!.apply {

            leftCorner = when (_leftCornerStyle) {
                0 ->    StaticRoundedCornerTreatment( leftCornerRadius )
                else -> StaticCutCornerTreatment( leftCornerRadius )
            }
            topLeftCorner = leftCorner

            rightCorner = when (_rightCornerStyle) {
                0 ->    StaticRoundedCornerTreatment( rightCornerRadius )
                else -> StaticCutCornerTreatment( rightCornerRadius )
            }
            topRightCorner = rightCorner
        }

        shapeDrawable.shapedViewModel = shapePathModel
    }

    class Behavior( context: Context, attrs: AttributeSet? )
        : BottomAppBar.Behavior( context, attrs ) {

        private val currentState: Int get() {
            var result = 0
            val hideBottomViewOnScrollBehaviorClass = this::class.java.superclass.superclass
            hideBottomViewOnScrollBehaviorClass.getDeclaredField("currentState").run {
                isAccessible = true
                result = get( this@Behavior ) as Int
                isAccessible = false
            }
            return result
        }
        private val BottomAppBar.material get() = this as MaterialBottomAppBar

        @Suppress("OverridingDeprecatedMember")
        override fun onNestedScroll(
                coordinatorLayout: CoordinatorLayout, child: BottomAppBar, target: View,
                dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int
        ) {
            if ( target.id == R.id.drawer_recycler_view ) return
            if ( dyConsumed > 0 )       slideDown( child )
            else if ( dyConsumed < 0)   slideUp(child)
        }

        override fun slideDown(child: BottomAppBar ) {
            child.material.run {
                if ( hideBarOnScroll && currentState != 1 ) super.slideDown( child )
                if ( hideFabOnScroll ) fab?.hide()
            }
        }

        override fun slideUp( child: BottomAppBar ) {
            child.material.run {
                if ( currentState != 2 ) super.slideUp( child )
                fab?.show()
            }
        }

    }

}