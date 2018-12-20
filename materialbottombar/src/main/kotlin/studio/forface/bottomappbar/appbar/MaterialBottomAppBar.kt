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
import androidx.core.view.postDelayed
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import studio.forface.bottomappbar.utils.dpToPixels
import studio.forface.bottomappbar.utils.findChildType
import studio.forface.bottomappbar.utils.reflection
import studio.forface.bottomappbar.utils.useAttributes
import studio.forface.bottomappbar.view.PanelView
import studio.forface.materialbottombar.bottomappbar.R

/**
 * @author Davide Giuseppe Farella
 * Inherit from [BottomAppBar]
 */
class MaterialBottomAppBar @JvmOverloads constructor (
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomAppBar( context, attrs, defStyleAttr ) {

    /**
     * An [Int] for keep track of the radius of the left corner
     * @see setCornersRadius
     */
    private var _leftCornerRadius: Int = 0

    /** An [Int] representing the corner style for the left corner */
    private var _leftCornerStyle: Int = 0

    /**
     * An [Int] for keep track of the radius of the right corner
     * @see setCornersRadius
     */
    private var _rightCornerRadius: Int = 0

    /** An [Int] representing the corner style for the right corner */
    private var _rightCornerStyle: Int = 0

    /**
     * Get ( [DrawableCompat.getAlpha] ) and Set ( [View.setAlpha] ) on [getBackground]
     * On Set, also re-set the [BottomAppBar.fabAlignmentMode]
     */
    var backgroundAlpha: Int
        get() = DrawableCompat.getAlpha( background )
        set( value ) {
            background.alpha = value
            fabAlignmentMode = fabAlignmentMode
        }

    /**
     * Get and Set the [StaticCornerTreatment.fixedInterpolation] of [leftCorner] and [rightCorner]
     *
     * Get: get the [StaticCornerTreatment.fixedInterpolation] only of the [leftCorner], since it
     * will be the same as [rightCorner]
     *
     * Set: se the [StaticCornerTreatment.fixedInterpolation] of [leftCorner] and [rightCorner] and
     * also re=set the [BottomAppBar.fabAlignmentMode]
     */
    var cornersInterpolation: Float
        get() = leftCorner.fixedInterpolation
        set( value ) {
            leftCorner.fixedInterpolation = value
            rightCorner.fixedInterpolation = value
            fabAlignmentMode = fabAlignmentMode
        }

    /** Get the OPTIONAL [FloatingActionButton] located in the [getParent] */
    val fab: FloatingActionButton? get() = ( parent as? ViewGroup )?.findChildType()

    /**
     *  A [Boolean] for keep track whether the [MaterialBottomAppBar] need to be hidden when a View
     *  is scrolled
     */
    var hideBarOnScroll: Boolean = false

    /** A [Boolean] for keep track whether the [fab] need to be hidden when a View is scrolled */
    var hideFabOnScroll: Boolean = false

    /**
     * Get the [behavior] casted as [MaterialBottomAppBar.Behavior].
     * Return null if behavior is not set yet ( not instance of [MaterialBottomAppBar.Behavior] ).
     */
    val layoutBehavior get() = ( layoutParams as CoordinatorLayout.LayoutParams )
            .behavior as? MaterialBottomAppBar.Behavior

    /** Get ( [View.getAlpha] ) and Set ( [View.setAlpha] ) on [navButtonView] */
    internal var menuIconAlpha: Float?
        get() = navButtonView?.alpha
        set( value ) { if ( value != null ) navButtonView?.alpha = value }

    /**
     * An [Animator] get via reflection from [BottomAppBar.modeAnimator], for handle custom
     * animations with the same [Animator] as [BottomAppBar]
     */
    private var modeAnimator: Animator? by reflection()

    /**
     * An [ImageButton] get via reflection from [BottomAppBar.mNavButtonView], for apply changes
     * to it
     */
    private val navButtonView: ImageButton? by reflection("mNavButtonView",2 )

    /** A [StaticCornerTreatment] for the left corner */
    internal lateinit var leftCorner: StaticCornerTreatment

    /** A [StaticCornerTreatment] for the right corner */
    internal lateinit var rightCorner: StaticCornerTreatment

    /* INITIALIZE THE VIEW */
    init {
        //fixNavigationIconPadding()
        val newHeight = dpToPixels(48f ).toInt()
        minimumHeight = newHeight

        // Before draw, apply to layoutParams
        doOnPreDraw {
            ( layoutParams as CoordinatorLayout.LayoutParams ).apply {
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

        // After draw, draw the top corners
        doOnLayout { drawBackgroundTopCorners() }
    }


    /**
     * Apply a new [StaticCornerTreatment] to [ShapeAppearanceModel.topLeftCorner] and
     * [ShapeAppearanceModel.topRightCorner] of [getBackground], using [_leftCornerRadius] and
     * [_rightCornerRadius]
     */
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

    /** Set a padding of 16dp to [navButtonView]. [View.setPadding] */
    private fun fixNavigationIconPadding() {
        val px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,16f, resources.displayMetrics ).toInt()
        navButtonView?.setPadding( px, px, px, px )
    }

    /** Flat or un-flat the corners whether [flat] is true or false  */
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

    /**
     * Override of [BottomAppBar.getHideOnScroll] only for mark it as deprecated since its usage
     * needs to be restricted
     */
    @Deprecated("use hideBarOnScroll", ReplaceWith("hideBarOnScroll" ) )
    override fun getHideOnScroll(): Boolean {
        return super.getHideOnScroll()
    }

    /** @see MaterialBottomAppBar.Behavior.hide */
    fun hide( withFab: Boolean = false , doOnAnimationEnd: () -> Unit = {} ) {
        layoutBehavior?.hide(this, withFab, doOnAnimationEnd )
    }

    /**
     * Call sequentially [MaterialBottomAppBar.Behavior.hide] and
     * [MaterialBottomAppBar.Behavior.show]
     *
     * @param doAfterHide the action to be executed after the bar is hidden
     * @param doAfterShow the action to be executed after the bar is show again
     */
    fun hideAndShow(
            withFab: Boolean = false, delay: Long = 150,
            doAfterHide: () -> Unit = {},
            doAfterShow: () -> Unit = {}
    ) {
        hide( withFab ) {
            doAfterHide()
            postDelayed( delay ) { show( doAfterShow ) }
        }
    }

    /**
     * Set the radius of the corners [_leftCornerRadius] and [_rightCornerRadius]
     * Also call [drawBackgroundTopCorners]
     */
    fun setCornersRadius( left: Int, right: Int ) {
        _leftCornerRadius = left
        _rightCornerRadius = right
        drawBackgroundTopCorners()
    }

    /**
     * Override of [BottomAppBar.setHideOnScroll] only for mark it as deprecated since its usage
     * needs to be restricted
     */
    @Deprecated("use hideBarOnScroll", ReplaceWith("hideBarOnScroll" ) )
    override fun setHideOnScroll( hide: Boolean ) {
        super.setHideOnScroll( hide )
    }

    /** @see MaterialBottomAppBar.Behavior.show */
    fun show( doOnAnimationEnd: () -> Unit = {} ) {
        layoutBehavior?.show(this, doOnAnimationEnd )
    }

    /** Unf-lat the corners. Call [flatCorners] with false as flat param */
    fun unFlatCorners() {
        flatCorners(false )
    }

    /**
     * A Custom behavior for [MaterialBottomAppBar].
     * Inherit from [BottomAppBar.Behavior]
     */
    class Behavior( context: Context, attrs: AttributeSet? )
        : BottomAppBar.Behavior( context, attrs ) {

        /**
         * Get the [ViewPropertyAnimator] via reflection from
         * [HideBottomViewOnScrollBehavior.currentAnimator]
         */
        private val currentAnimator by reflection<ViewPropertyAnimator?>( superclassLevel = 2 )

        /** Get an [Int] via reflection from [HideBottomViewOnScrollBehavior.currentState] */
        private val currentState by reflection<Int>( superclassLevel = 2 )

        /** Cast a [BottomAppBar] as [MaterialBottomAppBar] */
        private val BottomAppBar.material get() = this as MaterialBottomAppBar

        /** @see HideBottomViewOnScrollBehavior.onNestedScroll */
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

        /**
         * Hide the given [BottomAppBar] and the relative [FloatingActionButton] if [withFab] is
         * true
         *
         * @param child the [BottomAppBar] to hide
         *
         * @param withFab a [Boolean] representing whether the [FloatingActionButton] needs to be
         * hidden
         * @see MaterialBottomAppBar.fab
         *
         * @param doOnAnimationEnd a lambda that will be executed when the animation ends
         * @see [currentAnimator]
         * @see [ViewPropertyAnimator.withEndAction]
         */
        fun hide( child: BottomAppBar, withFab: Boolean, doOnAnimationEnd: () -> Unit ) {
            if ( withFab ) child.material.run {
                val oldHideFabOnScroll = hideFabOnScroll
                hideBarOnScroll = true
                slideDown( child )
                hideBarOnScroll = oldHideFabOnScroll

            } else super.slideDown( child )
            currentAnimator?.withEndAction( doOnAnimationEnd )
        }

        /** @see BottomAppBar.Behavior.slideDown */
        override fun slideDown( child: BottomAppBar ) {
            child.material.run {
                if ( hideBarOnScroll && currentState != 1 ) super.slideDown( child )
                if ( hideFabOnScroll ) fab?.hide()
            }
        }

        /**
         * Show the given [BottomAppBar] and its relative [FloatingActionButton].
         *
         * @param child the [BottomAppBar] to show
         *
         * @param doOnAnimationEnd a lambda that will be executed when the animation ends
         * @see [currentAnimator]
         * @see [ViewPropertyAnimator.withEndAction]
         */
        fun show( child: BottomAppBar, doOnAnimationEnd: () -> Unit ) {
            slideUp( child )
            currentAnimator?.withEndAction( doOnAnimationEnd )
        }

        /** @see BottomAppBar.Behavior.slideUp */
        override fun slideUp( child: BottomAppBar ) {
            child.material.run {
                if ( currentState != 2 ) super.slideUp( child )
                fab?.show()
            }
        }

    }
}