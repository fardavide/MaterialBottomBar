package studio.forface.bottomappbar.panels.items

import studio.forface.bottomappbar.panels.holders.SizeHolder
import studio.forface.bottomappbar.panels.holders.TextSizeHolder
import studio.forface.bottomappbar.panels.holders.TextStyleHolder

open class PrimaryPanelItem: BasePanelItem(), Cloneable {
    override val thisRef get() = this
    override val iconMarginStartDp = 24f
    override val iconMarginEndDp = 24f
    override val iconAlpha = 0.7f

    override var titleTextSizeHolder =  TextSizeHolder( sp = 18f )
    override var titleTextStyleHolder = TextStyleHolder( bold = true )
    override var iconSizeHolder =       SizeHolder( dp = 24f )

    fun cloneRef() = clone() as PrimaryPanelItem
}