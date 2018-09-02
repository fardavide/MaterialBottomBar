package studio.forface.bottomappbar.drawer.items.extra

import studio.forface.bottomappbar.panels.holders.*
import studio.forface.bottomappbar.panels.items.extra.SimpleExtraItem

open class BadgeItem: SimpleExtraItem<BadgeItem>() {
    override val thisRef get() = this
    override fun newInstance() = BadgeItem()

    override var contentTextStyleHolder =           TextStyleHolder( bold = true )
    override var contentTextSizeHolder =            TextSizeHolder( sp = 9f )

    override var backgroundCornerRadiusSizeHolder = SizeHolder( dp = 6f )
}