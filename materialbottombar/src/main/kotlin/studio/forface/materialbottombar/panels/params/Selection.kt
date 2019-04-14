@file:Suppress("unused")

package studio.forface.materialbottombar.panels.params

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.doOnPreDraw
import studio.forface.materialbottombar.dsl.DslComponent
import studio.forface.materialbottombar.dsl.dsl
import studio.forface.materialbottombar.panels.holders.ColorHolder
import studio.forface.materialbottombar.panels.holders.SizeHolder
import studio.forface.materialbottombar.utils.Drawables

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that have a customizable [Selection]
 *
 * Inherit from [Param]
 */
interface Selection<T>: Param<T> {

    /** A reference to [ColorHolder] fot the Selection's Background */
    var selectionBackgroundColorHolder: ColorHolder

    /** A reference to [ColorHolder] fot the Selection's Icon */
    var selectionIconColorHolder: ColorHolder

    /** A reference to [ColorHolder] fot the Selection's Ripple */
    var selectionRippleColorHolder: ColorHolder

    /** A reference to [ColorHolder] fot the Selection's Title */
    var selectionTitleColorHolder: ColorHolder

    /** A reference to [SizeHolder] fot the Selection */
    var selectionCornerRadiusSizeHolder: SizeHolder

    /** An [OnItemClickListener] for the Items */
    var onItemClick: OnItemClickListener

    /**
     *  Apply all the [Selection]'s params to the given [View]s
     *  @param selected whether the Item must be selected
     *
     *  @see applySelectionBackgroundTo
     *  @see applySelectionIconTo
     *  @see applySelectionTitleTo
     */
    fun applySelectionTo( container: View, icon: ImageView, title: TextView, selected: Boolean = false ) {
        applySelectionBackgroundTo( container, selected )
        applySelectionIconTo( icon, selected )
        applySelectionTitleTo( title, selected )
    }

    /**
     *  Apply the [Selection]'s Background and Ripple to the given [View]
     *  @param selected whether the Item must be selected
     */
    fun applySelectionBackgroundTo( view: View, selected: Boolean = false ) {
        view.doOnPreDraw {
            // Get colors
            val tempBackgroundColor = selectionBackgroundColorHolder.resolveColor( view.context )
            val tempRippleColor = selectionRippleColorHolder.resolveColor( view.context )
            // Return if both of them are null
            if ( tempBackgroundColor == null && tempRippleColor == null ) return@doOnPreDraw
            val backgroundColor = tempBackgroundColor ?: tempRippleColor!!
            val rippleColor = tempRippleColor ?: tempBackgroundColor!!

            // Calculate the corners
            val cornerLimit = Math.min( view.height, view.width ) / 2
            val cornerRadius = selectionCornerRadiusSizeHolder.resolveFloatSize()
                    ?.coerceAtMost( cornerLimit.toFloat() )
                    ?: Drawables.CORNER_RADIUS_SOFT

            // Build the background
            val background = if ( selected )
                Drawables.materialDrawable( backgroundColor, cornerRadius,0.3f )
            else Drawables.selectableDrawable( backgroundColor, rippleColor, cornerRadius )

            // Apply the background
            view.background = background
        }
    }

    /**
     *  Apply the [Selection]'s Icon to the given [ImageView]
     *  @param selected whether the Item must be selected
     */
    fun applySelectionIconTo( imageView: ImageView, selected: Boolean = false ) {
        if ( selected ) selectionIconColorHolder.applyToImageView(imageView)
    }

    /**
     *  Apply the [Selection]'s Title to the given [TextView]
     *  @param selected whether the Item must be selected
     */
    fun applySelectionTitleTo( textView: TextView, selected: Boolean = false ) {
        if ( selected ) selectionTitleColorHolder.applyToTextView( textView )
    }

    /**
     * Apply the [OnItemClickListener] to the [Selection]
     * @return [T]
     */
    fun itemClickListener( onItemClickListener: OnItemClickListener ) =
            thisRef.apply { this@Selection.onItemClick = onItemClickListener }


    /**
     * Apply the [ColorInt] to the [selectionBackgroundColorHolder]
     * @return [T]
     */
    fun selectionBackgroundColor( @ColorInt color: Int ) =
            thisRef.apply { selectionBackgroundColorHolder = ColorHolder( color = color ) }

    /**
     * Apply the [ColorRes] to the [selectionBackgroundColorHolder]
     * @return [T]
     */
    fun selectionBackgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { selectionBackgroundColorHolder = ColorHolder( colorRes = res ) }

    /**
     * Apply the [ColorInt] to the [selectionIconColorHolder]
     * @return [T]
     */
    fun selectionIconColor( @ColorInt color: Int ) =
            thisRef.apply { selectionIconColorHolder = ColorHolder( color = color ) }

    /**
     * Apply the [ColorRes] to the [selectionIconColorHolder]
     * @return [T]
     */
    fun selectionIconColorRes( @ColorRes res: Int ) =
            thisRef.apply { selectionIconColorHolder = ColorHolder( colorRes = res ) }

    /**
     * Apply the [ColorInt] to the [selectionRippleColorHolder]
     * @return [T]
     */
    fun selectionRippleColor( @ColorInt color: Int ) =
            thisRef.apply { selectionRippleColorHolder = ColorHolder( color = color ) }

    /**
     * Apply the [ColorRes] to the [selectionRippleColorHolder]
     * @return [T]
     */
    fun selectionRippleColorRes( @ColorRes res: Int ) =
            thisRef.apply { selectionRippleColorHolder = ColorHolder( colorRes = res ) }

    /**
     * Apply the [ColorInt] to the [selectionTitleColorHolder]
     * @return [T]
     */
    fun selectionTitleColor( @ColorInt color: Int ) =
            thisRef.apply { selectionTitleColorHolder = ColorHolder( color = color ) }

    /**
     * Apply the [ColorRes] to the [selectionTitleColorHolder]
     * @return [T]
     */
    fun selectionTitleColorRes( @ColorRes res: Int ) =
            thisRef.apply { selectionTitleColorHolder = ColorHolder( colorRes = res ) }


    /**
     * Apply the [Float] Size in DP to the [selectionCornerRadiusSizeHolder]
     * @return [T]
     */
    fun selectionCornerRadiusDp( dp: Float ) =
            thisRef.apply { selectionCornerRadiusSizeHolder = SizeHolder( dp = dp ) }

    /**
     * Apply the [Float] Size in Pixel to the [selectionCornerRadiusSizeHolder]
     * @return [T]
     */
    fun selectionCornerRadiusPixel( pixel: Float ) =
            thisRef.apply { selectionCornerRadiusSizeHolder = SizeHolder( pixel = pixel ) }
}

/** A typealias for a lambda that take an [Int] Id and a [CharSequence] Title and return [Unit] */
typealias OnItemClickListener = (id: Int, title: CharSequence) -> Unit


/** A [DslComponent] for call [Selection.selectionBackgroundColor] function */
var Selection<*>.selectionBackgroundColor: Int by dsl { selectionBackgroundColor( it ) }

/** A [DslComponent] for call [Selection.selectionBackgroundColorRes] function */
var Selection<*>.selectionBackgroundColorRes: Int by dsl { selectionBackgroundColorRes( it ) }

/** A [DslComponent] for call [Selection.selectionIconColor] function */
var Selection<*>.selectionIconColor: Int by dsl { selectionIconColor( it ) }

/** A [DslComponent] for call [Selection.selectionIconColorRes] function */
var Selection<*>.selectionIconColorRes: Int by dsl { selectionIconColorRes( it ) }

/** A [DslComponent] for call [Selection.selectionRippleColor] function */
var Selection<*>.selectionRippleColor: Int by dsl { selectionRippleColor( it ) }

/** A [DslComponent] for call [Selection.selectionRippleColorRes] function */
var Selection<*>.selectionRippleColorRes: Int by dsl { selectionRippleColorRes( it ) }

/** A [DslComponent] for call [Selection.selectionTitleColor] function */
var Selection<*>.selectionTitleColor: Int by dsl { selectionTitleColor( it ) }

/** A [DslComponent] for call [Selection.selectionTitleColorRes] function */
var Selection<*>.selectionTitleColorRes: Int by dsl { selectionTitleColorRes( it ) }

/** A [DslComponent] for call [Selection.selectionCornerRadiusDp] function */
var Selection<*>.selectionCornerRadiusDp: Float by dsl { selectionCornerRadiusDp( it ) }

/** A [DslComponent] for call [Selection.selectionCornerRadiusPixel] function */
var Selection<*>.selectionCornerRadiusPixel: Float by dsl { selectionCornerRadiusPixel( it ) }