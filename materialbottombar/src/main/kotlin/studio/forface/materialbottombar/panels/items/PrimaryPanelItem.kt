package studio.forface.materialbottombar.panels.items

import studio.forface.materialbottombar.panels.holders.SizeHolder
import studio.forface.materialbottombar.panels.holders.TextSizeHolder
import studio.forface.materialbottombar.panels.holders.TextStyleHolder

open class PrimaryPanelItem: BasePanelItem() {
    override val thisRef get() = this
    override val iconMarginStartDp = 24f
    override val iconMarginEndDp = 24f
    override val iconAlpha = 0.7f

    override var titleTextSizeHolder =  TextSizeHolder( sp = 18f )
    override var titleTextStyleHolder = TextStyleHolder( bold = true )
    override var iconSizeHolder =       SizeHolder( dp = 24f )

    public override fun clone(): PrimaryPanelItem {
        val item = super.clone() as PrimaryPanelItem
        item.badgeItem = badgeItem.cloneRef()
        item.buttonItem = buttonItem.cloneRef()
        return item
    }
}