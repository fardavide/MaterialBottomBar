package studio.forface.bottomappbar.materialbottomdrawer.params

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import studio.forface.bottomappbar.materialbottomdrawer.holders.ColorHolder
import studio.forface.bottomappbar.utils.Drawables

typealias OnItemClickListener = (id: Int, title: CharSequence) -> Unit

interface Selection<T>: Param<T> {
    var selectionColorHolder: ColorHolder
    var roundedCorners: Boolean

    var onItemClickListener: OnItemClickListener

    fun applySelectionTo( view: View, selected: Boolean = false ) {
        val color = selectionColorHolder.resolveColor( view.context )
        val cornerRadius = if ( roundedCorners ) Drawables.CORNER_RADIUS_SOFT else 0f

        color?.let {
            val background = if ( selected )
                Drawables.materialDrawable( color, cornerRadius,true )
            else Drawables.selectableDrawable( color, cornerRadius )

            view.background = background
        }
    }

    fun itemClickListener( onItemClickListener: OnItemClickListener ) =
            thisRef.apply { this@Selection.onItemClickListener = onItemClickListener }

    fun selectionColorRes( @ColorRes res: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( colorRes = res ) }
    fun selectionColor( @ColorInt color: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( color = color ) }

    fun selectionRounderCorners( rounded: Boolean = true ) =
            this.apply { this@Selection.roundedCorners = roundedCorners }
}