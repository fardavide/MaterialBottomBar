package studio.forface.bottomappbar.materialpanels.params

import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.doOnPreDraw
import studio.forface.bottomappbar.materialpanels.holders.ColorHolder
import studio.forface.bottomappbar.materialpanels.holders.SizeHolder
import studio.forface.bottomappbar.utils.Drawables

enum class RippleBackgroundStyle { COLOR, FLAT }

interface Background<T>: Param<T> {
    var backgroundColorHolder:              ColorHolder
    var backgroundCornerRadiusSizeHolder:   SizeHolder

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

    fun backgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( colorRes = res ) }
    fun backgroundColorHex( hex: String ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( colorHex = hex ) }
    fun backgroundColor( @ColorInt color: Int ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( color = color ) }

    fun backgroundCornerRadiusPixel( pixel: Float ) =
            thisRef.apply { backgroundCornerRadiusSizeHolder = SizeHolder( pixel = pixel ) }
    fun backgroundCornerRadiusDp( dp: Float ) =
            thisRef.apply { backgroundCornerRadiusSizeHolder = SizeHolder( dp = dp ) }
}