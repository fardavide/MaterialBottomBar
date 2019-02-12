package studio.forface.materialbottombar.panels.items

import studio.forface.materialbottombar.panels.holders.ColorHolder
import studio.forface.materialbottombar.panels.holders.SizeHolder
import studio.forface.materialbottombar.panels.holders.TextSizeHolder

abstract class AbsSecondaryPanelItem<T: AbsSecondaryPanelItem<T>>: BasePanelItem<T>() {
    override val iconMarginStartDp = 48f
    override val iconMarginEndDp = 12f
    override val iconAlpha = 0.5f

    override var titleTextSizeHolder =  TextSizeHolder( sp = 14f )

    override var iconColorHolder =      ColorHolder()
    override var iconSizeHolder =       SizeHolder( dp = 20f )

    public override fun clone(): T {
        @Suppress("UNCHECKED_CAST")
        val item = super.clone() as T
        item.badgeItem = badgeItem.cloneRef()
        item.buttonItem = buttonItem.cloneRef()
        return item
    }
}

/**
 * A [BasePanelItem] that is styled as secondary item
 * Inherit from [AbsSecondaryPanelItem]
 */
open class SecondaryPanelItem: AbsSecondaryPanelItem<SecondaryPanelItem>() {
    override val thisRef get() = this
}

/** A typealias of [SecondaryPanelItem] for Drawer */
typealias SecondaryDrawerItem = SecondaryPanelItem