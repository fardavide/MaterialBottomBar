@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package studio.forface.bottomappbar.appbar

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.ImageButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import studio.forface.bottomappbar.utils.dpToPixels
import studio.forface.bottomappbar.utils.findChildType
import studio.forface.bottomappbar.utils.reflection
import studio.forface.bottomappbar.utils.useAttributes
import studio.forface.bottomappbar.view.PanelView
import studio.forface.materialbottombar.bottomappbar.R

/**
 * @author Davide Giuseppe Farella
 */
class MaterialBottomAppBar @JvmOverloads constructor (
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomAppBar( context, attrs, defStyleAttr ) {

    val fab: FloatingActionButton? get() = ( parent as? ViewGroup )?.findChildType()

    var hideBarOnScroll: Boolean = false
    var hideFabOnScroll: Boolean = false

    @Deprecated("use hideBarOnScroll", ReplaceWith("hideBarOnScroll" ) )
    override fun getHideOnScroll(): Boolean {
        return super.getHideOnScroll()
    }
    @Deprecated("use hideBarOnScroll", ReplaceWith("hideBarOnScroll" ) )
    override fun setHideOnScroll( hide: Boolean ) {
        super.setHideOnScroll( hide )
    }

    private var _leftCornerRadius: Int = 0
    private var _leftCornerStyle: Int = 0
    private var _rightCornerRadius: Int = 0
    private var _rightCornerStyle: Int = 0

    private val navView: ImageButton? by reflection( "mNavButtonView",2 )
    private var modeAnimator: Animator? by reflection()

    internal lateinit var leftCorner: StaticCornerTreatment
    internal lateinit var rightCorner: StaticCornerTreatment


    fun setCornersRadius( left: Int, right: Int ) {
        _leftCornerRadius = left
        _rightCornerRadius = right
        drawBackgroundTopCorners()
    }

    internal var menuIconAlpha: Float?
        get() = navView?.alpha
        set( value ) { if ( value != null ) navView?.alpha = value }

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
        fabAlignmentMode = fabAlignmentMode
    }

    var cornersInterpolation: Float
        get() = leftCorner.fixedInterpolation
        set( value ) {
            leftCorner.fixedInterpolation = value
            rightCorner.fixedInterpolation = value
            fabAlignmentMode = fabAlignmentMode
        }

    var backgroundAlpha: Int
        get() = DrawableCompat.getAlpha( background )
        set( value ) {
            background.alpha = value
            fabAlignmentMode = fabAlignmentMode
        }

    init {
        //fixNavigationIconPadding()
        val newHeight = dpToPixels(48f ).toInt()
        minimumHeight = newHeight

        doOnPreDraw {
            (layoutParams as CoordinatorLayout.LayoutParams).apply {
                if ( height < newHeight ) height = newHeight
                minimumHeight = height
                behavior = Behavior( context, attrs )
                hideBarOnScroll = hideOnScroll
                hideOnScroll = true
            }
        }

        context.useAttributes( attrs, R.styleable.MaterialBottomAppBar ) {
            hideFabOnScroll = getBoolean(
                    R.styleable.MaterialBottomAppBar_hideFabOnScroll, false )

            _leftCornerRadius = getDimensionPixelSize(
                    R.styleable.MaterialBottomAppBar_leftCornerRadius,0 )
            _leftCornerStyle = getInt(
                    R.styleable.MaterialBottomAppBar_leftCornerStyle, 0 )

            _rightCornerRadius = getDimensionPixelSize(
                    R.styleable.MaterialBottomAppBar_rightCornerRadius,0 )
            _rightCornerStyle = getInt(
                    R.styleable.MaterialBottomAppBar_rightCornerStyle,0 )
        }

        doOnLayout { drawBackgroundTopCorners() }
    }

    val layoutBehavior get() =
        ( ( layoutParams as CoordinatorLayout.LayoutParams ).behavior as? MaterialBottomAppBar.Behavior )

    fun hideAndShow(
            withFab: Boolean = false, delay: Long = 150,
            doAfterHide: () -> Unit = {},
            doAfterShow: () -> Unit = {}
    ) {
        hide( withFab ) { doAfterHide(); postDelayed( { show( doAfterShow ) } , delay) }
    }

    fun hide( withFab: Boolean = false , doOnAnimationEnd: () -> Unit = {} ) {
        layoutBehavior?.hide(this, withFab, doOnAnimationEnd )
    }

    fun show( doOnAnimationEnd: () -> Unit = {} ) {
        layoutBehavior?.show(this, doOnAnimationEnd )
    }

    private fun fixNavigationIconPadding() {
        val px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,16f, resources.displayMetrics ).toInt()
        navView?.setPadding( px, px, px, px )
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

        shapeDrawable.shapeAppearanceModel = shapePathModel
    }

    class Behavior( context: Context, attrs: AttributeSet? )
        : BottomAppBar.Behavior( context, attrs ) {

        private val currentAnimator by reflection<ViewPropertyAnimator?>( superclassLevel = 2 )
        private val currentState by reflection<Int>( superclassLevel = 2 )

        private val BottomAppBar.material get() = this as MaterialBottomAppBar

        @Suppress("OverridingDeprecatedMember")
        override fun onNestedScroll(
                coordinatorLayout: CoordinatorLayout, child: BottomAppBar, target: View,
                dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int
        ) {
            var targetParent = target.parent
            while ( targetParent != null && targetParent !is PanelView )
                targetParent = targetParent.parent
            if ( targetParent is PanelView ) return

            if ( dyConsumed > 0 )       slideDown( child )
            else if ( dyConsumed < 0)   slideUp(child)
        }

        fun hide( child: BottomAppBar, withFab: Boolean, doOnAnimationEnd: () -> Unit ) {
            if ( withFab ) child.material.run {
                val oldHideFabOnScroll = hideFabOnScroll
                hideBarOnScroll = true
                slideDown( child )
                hideBarOnScroll = oldHideFabOnScroll

            } else super.slideDown( child )
            currentAnimator?.withEndAction( doOnAnimationEnd )
        }

        override fun slideDown( child: BottomAppBar ) {
            child.material.run {
                if ( hideBarOnScroll && currentState != 1 ) super.slideDown( child )
                if ( hideFabOnScroll ) fab?.hide()
            }
        }

        fun show( child: BottomAppBar, doOnAnimationEnd: () -> Unit ) {
            slideUp( child )
            currentAnimator?.withEndAction( doOnAnimationEnd )
        }
        override fun slideUp( child: BottomAppBar ) {
            child.material.run {
                if ( currentState != 2 ) super.slideUp( child )
                fab?.show()
            }
        }
    }

}