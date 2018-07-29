package studio.forface.bottomappbar.materialbottomdrawer.draweritems

import studio.forface.bottomappbar.materialbottomdrawer.holders.IconSizeHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.TextSizeHolder

open class PrimaryDrawerItem: BaseDrawerItem() {
    override val thisRef get() = this
    override val iconMarginStartDp = 24f
    override val iconMarginEndDp = 24f
    override val iconAlpha = 0.7f

    override var titleTextSizeHolder =  TextSizeHolder( sp = 18f )

    override var iconSizeHolder =       IconSizeHolder( dp = 24f )
}