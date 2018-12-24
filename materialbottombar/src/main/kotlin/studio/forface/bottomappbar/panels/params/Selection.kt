@file:Suppress("unused")

package studio.forface.bottomappbar.panels.params

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.doOnPreDraw
import studio.forface.bottomappbar.dsl.DslComponent
import studio.forface.bottomappbar.dsl.dsl
import studio.forface.bottomappbar.panels.holders.ColorHolder
import studio.forface.bottomappbar.panels.holders.SizeHolder
import studio.forface.bottomappbar.utils.Drawables

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that have a customizable [Selection]
 *
 * Inherit from [Param]
 */
interface Selection<T>: Param<T> {

    /** A reference to [ColorHolder] fot the Selection */
    var selectionColorHolder: ColorHolder
    /** A reference to [SizeHolder] fot the Selection */
    var selectionCornerRadiusSizeHolder: SizeHolder

    /** An [OnItemClickListener] for the Items */
    var onItemClick: OnItemClickListener

    /**
     *  Apply the [Selection] to the given [View]
     *  @param selected whether the Item must be selected
     */
    fun applySelectionTo( view: View, selected: Boolean = false ) {
        view.doOnPreDraw {
            val color = selectionColorHolder.resolveColor( view.context )
            val cornerLimit = Math.min( view.height, view.width ) / 2
            val cornerRadius = selectionCornerRadiusSizeHolder.resolveFloatSize()
                    ?.coerceAtMost( cornerLimit.toFloat() )
                    ?: Drawables.CORNER_RADIUS_SOFT

            color?.let {
                val background = if ( selected )
                    Drawables.materialDrawable( color, cornerRadius,0.3f )
                else Drawables.selectableDrawable( color, cornerRadius )

                view.background = background
            }
        }
    }

    /**
     * Apply the [OnItemClickListener] to the [Selection]
     * @return [T]
     */
    fun itemClickListener( onItemClickListener: OnItemClickListener ) =
            thisRef.apply { this@Selection.onItemClick = onItemClickListener }


    /**
     * Apply the [ColorInt] to the [selectionColorHolder]
     * @return [T]
     */
    fun selectionColor( @ColorInt color: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( color = color ) }

    /**
     * Apply the [ColorRes] to the [selectionColorHolder]
     * @return [T]
     */
    fun selectionColorRes( @ColorRes res: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( colorRes = res ) }


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


/** A [DslComponent] for call [Selection.selectionColor] function */
var Selection<*>.selectionColor: Int by dsl { selectionColor(it ) }

/** A [DslComponent] for call [Selection.selectionColorRes] function */
var Selection<*>.selectionColorRes: Int by dsl { selectionColorRes(it ) }

/** A [DslComponent] for call [Selection.selectionCornerRadiusDp] function */
var Selection<*>.selectionCornerRadiusDp: Float by dsl { selectionCornerRadiusDp( it ) }

/** A [DslComponent] for call [Selection.selectionCornerRadiusPixel] function */
var Selection<*>.selectionCornerRadiusPixel: Float by dsl { selectionCornerRadiusPixel( it ) }