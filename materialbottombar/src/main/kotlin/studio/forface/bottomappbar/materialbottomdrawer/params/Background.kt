package studio.forface.bottomappbar.materialbottomdrawer.params

import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.doOnPreDraw
import studio.forface.bottomappbar.materialbottomdrawer.holders.ColorHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.SizeHolder
import studio.forface.bottomappbar.utils.Drawables

interface Background<T>: Param<T> {
    var backgroundColorHolder:              ColorHolder
    var backgroundCornerRadiusSizeHolder:    SizeHolder

    fun applyBackgroundTo( view: View ) {
        view.doOnPreDraw {
            val color = backgroundColorHolder.resolveColor(view.context) ?: Color.TRANSPARENT
            val cornerLimit = Math.min(view.height, view.width) / 2
            val cornerRadius = (backgroundCornerRadiusSizeHolder.resolveFloatSize() ?: 0f)
                    .coerceAtMost(cornerLimit.toFloat())

            view.background = Drawables.materialDrawable( color, cornerRadius )
        }
    }

    fun backgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( colorRes = res ) }
    fun backgroundColor( @ColorInt color: Int ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( color = color ) }

    fun backgroundCornerRadiusPixel(pixel: Float ) =
            thisRef.apply { backgroundCornerRadiusSizeHolder = SizeHolder( pixel = pixel ) }
    fun backgroundCornerRadiusDp(dp: Float ) =
            thisRef.apply { backgroundCornerRadiusSizeHolder = SizeHolder( dp = dp ) }
}