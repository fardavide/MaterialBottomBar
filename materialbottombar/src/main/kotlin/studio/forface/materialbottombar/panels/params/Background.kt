@file:Suppress("unused")

package studio.forface.materialbottombar.panels.params

import android.graphics.Color
import android.view.View
import android.widget.Button
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
 * An interface for items that have a customizable Background.
 * Inherit from [Param]
 */
interface Background<T>: Param<T> {

    /** A reference to a [ColorHolder] for the Background Color */
    var backgroundColorHolder: ColorHolder
    /** A reference to a [SizeHolder] for the Corner Radius of the Background */
    var backgroundCornerRadiusSizeHolder: SizeHolder

    /**
     * Apply the [Background] to the given [View].
     * @param scaleToSquare if true, will set the Width of the [View] to match the Height.
     */
    fun applyBackgroundTo( view: View, scaleToSquare: Boolean = false ) {
        view.doOnPreDraw {
            if ( scaleToSquare )
                view.layoutParams.apply { this.width = view.height }

            val color = backgroundColorHolder.resolveColor( view.context ) ?: Color.TRANSPARENT
            val minSize = if ( scaleToSquare ) view.height else Math.min( view.height, view.width )
            val cornerLimit = minSize / 2
            val cornerRadius = ( backgroundCornerRadiusSizeHolder.resolveFloatSize() ?: 0f )
                    .coerceAtMost( cornerLimit.toFloat() )

            val background = if ( view is Button )
                Drawables.selectableDrawable( color, cornerRadius,false )
            else Drawables.materialDrawable( color, cornerRadius )

            view.background = background
        }
    }

    /**
     * Set the [backgroundColorHolder] with the given [ColorInt]
     * @return [T]
     */
    fun backgroundColor( @ColorInt color: Int ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( color = color ) }

    /**
     * Set the [backgroundColorHolder] with the given color HEX [String]
     * @return [T]
     */
    fun backgroundColorHex( hex: String ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( colorHex = hex ) }

    /**
     * Set the [backgroundColorHolder] with the given [ColorRes]
     * @return [T]
     */
    fun backgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( colorRes = res ) }


    /**
     * Set the [backgroundCornerRadiusSizeHolder] with the given size in DP [Float]
     * @return [T]
     */
    fun backgroundCornerRadiusDp( dp: Float ) =
            thisRef.apply { backgroundCornerRadiusSizeHolder = SizeHolder( dp = dp ) }

    /**
     * Set the [backgroundCornerRadiusSizeHolder] with the given size in pixel [Float]
     * @return [T]
     */
    fun backgroundCornerRadiusPixel( pixel: Float ) =
            thisRef.apply { backgroundCornerRadiusSizeHolder = SizeHolder( pixel = pixel ) }
}

/** An enum containing Styles of [Background] */
enum class RippleBackgroundStyle { COLOR, FLAT }

/** A [DslComponent] for call [Background.backgroundColor] function */
var Background<*>.backgroundColor: Int by dsl { backgroundColor( it ) }

/** A [DslComponent] for call [Background.backgroundColorHex] function */
var Background<*>.backgroundColorHex: String by dsl { backgroundColorHex( it ) }

/** A [DslComponent] for call [Background.backgroundColorRes] function */
var Background<*>.backgroundColorRes: Int by dsl { backgroundColorRes( it ) }

/** A [DslComponent] for call [Background.backgroundCornerRadiusDp] function */
var Background<*>.backgroundCornerRadiusDp: Float by dsl { backgroundCornerRadiusDp( it ) }

/** A [DslComponent] for call [Background.backgroundCornerRadiusPixel] function */
var Background<*>.backgroundCornerRadiusPixel: Float by dsl { backgroundCornerRadiusPixel( it ) }