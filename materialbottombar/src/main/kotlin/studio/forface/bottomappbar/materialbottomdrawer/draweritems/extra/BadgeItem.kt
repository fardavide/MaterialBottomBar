package studio.forface.bottomappbar.materialbottomdrawer.draweritems.extra

import studio.forface.bottomappbar.materialbottomdrawer.holders.*

open class BadgeItem: SimpleExtraItem<BadgeItem>() {
    override val thisRef get() = this
    override fun newInstance() = BadgeItem()

    override var contentTextStyleHolder =           TextStyleHolder( bold = true )
    override var contentTextSizeHolder =            TextSizeHolder( sp = 9f )

    override var backgroundCornerRadiusSizeHolder = SizeHolder( dp = 6f )
}