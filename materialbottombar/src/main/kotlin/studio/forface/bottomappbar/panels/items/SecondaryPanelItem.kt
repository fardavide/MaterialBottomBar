package studio.forface.bottomappbar.panels.items

import studio.forface.bottomappbar.panels.holders.ColorHolder
import studio.forface.bottomappbar.panels.holders.SizeHolder
import studio.forface.bottomappbar.panels.holders.TextSizeHolder

open class SecondaryPanelItem: BasePanelItem() {
    override val thisRef get() = this
    override val iconMarginStartDp = 48f
    override val iconMarginEndDp = 12f
    override val iconAlpha = 0.5f

    override var titleTextSizeHolder =  TextSizeHolder( sp = 14f )

    override var iconColorHolder =      ColorHolder()
    override var iconSizeHolder =       SizeHolder( dp = 20f )

    public override fun clone(): SecondaryPanelItem {
        val item = super.clone() as SecondaryPanelItem
        item.badgeItem = badgeItem.cloneRef()
        item.buttonItem = buttonItem.cloneRef()
        return item
    }
}