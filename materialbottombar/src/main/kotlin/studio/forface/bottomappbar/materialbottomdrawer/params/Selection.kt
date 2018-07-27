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

    fun applyTo( view: View, selected: Boolean = false ) {
        val color = selectionColorHolder.resolveColor( view.context )
        color?.let {
            val background = if ( selected )
                Drawables.getRippleColor( color, roundedCorners,true )
            else Drawables.getSelectableDrawableFor( color, roundedCorners )

            view.background = background
        }
    }

    fun withItemClickListener( onItemClickListener: OnItemClickListener ) =
            thisRef.apply { this@Selection.onItemClickListener = onItemClickListener }

    fun withSelectionColorRes( @ColorRes res: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( colorRes = res ) }

    fun withSelectionColor( @ColorInt color: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( color = color ) }

    fun withSelectionRounderCorners(rounded: Boolean = true ) {
        roundedCorners = rounded
    }
}