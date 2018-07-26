package studio.forface.bottomappbar.materialbottomdrawer.params

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import studio.forface.bottomappbar.materialbottomdrawer.holders.ColorHolder
import studio.forface.bottomappbar.utils.Drawables

interface Selection<T>: Param<T> {
    var selectionColorHolder: ColorHolder
    var roundedCorners: Boolean

    fun applyTo( view: View ) {
        val color = selectionColorHolder.resolveColor( view.context )
        color?.let { view.background = Drawables.getSelectableDrawableFor( color, roundedCorners ) }
    }

    fun withSelectionColorRes( @ColorRes res: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( colorRes = res ) }

    fun withSelectionColor( @ColorInt color: Int ) =
            thisRef.apply { selectionColorHolder = ColorHolder( color = color ) }

    fun withSelectionRounderCorners(rounded: Boolean = true ) {
        roundedCorners = rounded
    }
}