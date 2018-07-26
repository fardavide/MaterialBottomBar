package studio.forface.bottomappbar.materialbottomdrawer.params

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import studio.forface.bottomappbar.materialbottomdrawer.holders.ColorHolder

interface Background<T>: Param<T> {
    var backgroundColorHolder: ColorHolder

    fun withBackgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( colorRes = res ) }

    fun withBackgroundColor( @ColorInt color: Int ) =
            thisRef.apply { backgroundColorHolder = ColorHolder( color = color ) }
}