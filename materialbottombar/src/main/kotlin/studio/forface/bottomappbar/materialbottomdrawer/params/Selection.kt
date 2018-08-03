package studio.forface.bottomappbar.materialbottomdrawer.params

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.doOnPreDraw
import studio.forface.bottomappbar.materialbottomdrawer.holders.ColorHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.SizeHolder
import studio.forface.bottomappbar.utils.Drawables

typealias OnItemClickListener = (id: Int, title: CharSequence) -> Unit

interface Selection<T>: Param<T> {
    var selectionColorHolder:               ColorHolder
    var selectionCornerRadiusSizeHolder:    SizeHolder

    var onItemClickListener: OnItemClickListener

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

    fun itemClickListener( onItemClickListener: OnItemClickListener ) =
            thisRef.apply { this@Selection.onItemClickListener = onItemClickListener }

    fun selectionColorRes( @ColorRes res: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( colorRes = res ) }
    fun selectionColor( @ColorInt color: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( color = color ) }

    fun selectionCornerRadiusPixel(pixel: Float ) =
            thisRef.apply { selectionCornerRadiusSizeHolder = SizeHolder( pixel = pixel ) }
    fun selectionCornerRadiusDp(dp: Float ) =
            thisRef.apply { selectionCornerRadiusSizeHolder = SizeHolder( dp = dp ) }
}