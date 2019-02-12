package studio.forface.materialbottombar.panels.items

import studio.forface.materialbottombar.panels.holders.SizeHolder
import studio.forface.materialbottombar.panels.holders.TextSizeHolder
import studio.forface.materialbottombar.panels.holders.TextStyleHolder

abstract class AbsPrimaryPanelItem<T: AbsPrimaryPanelItem<T>>: BasePanelItem<T>() {
    override val iconMarginStartDp = 24f
    override val iconMarginEndDp = 24f
    override val iconAlpha = 0.7f

    override var titleTextSizeHolder =  TextSizeHolder( sp = 18f )
    override var titleTextStyleHolder = TextStyleHolder( bold = true )
    override var iconSizeHolder =       SizeHolder( dp = 24f )

    public override fun clone(): T {
        @Suppress("UNCHECKED_CAST")
        val item = super.clone() as T
        item.badgeItem = badgeItem.cloneRef()
        item.buttonItem = buttonItem.cloneRef()
        return item
    }
}

/**
 * A [BasePanelItem] that is styled as primary item
 * Inherit from [AbsPrimaryPanelItem]
 */
open class PrimaryPanelItem: AbsPrimaryPanelItem<PrimaryPanelItem>() {
    override val thisRef get() = this
}

/** A typealias of [PrimaryPanelItem] for Drawer */
typealias PrimaryDrawerItem = PrimaryPanelItem