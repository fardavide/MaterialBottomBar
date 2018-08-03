package studio.forface.bottomappbar.materialbottomdrawer.draweritems

import studio.forface.bottomappbar.materialbottomdrawer.holders.SizeHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.TextSizeHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.TextStyleHolder

open class PrimaryDrawerItem: BaseDrawerItem() {
    override val thisRef get() = this
    override val iconMarginStartDp = 24f
    override val iconMarginEndDp = 24f
    override val iconAlpha = 0.7f

    override var titleTextSizeHolder =  TextSizeHolder( sp = 18f )
    override var titleTextStyleHolder = TextStyleHolder( bold = true )
    override var iconSizeHolder =       SizeHolder( dp = 24f )
}