package studio.forface.bottomappbar.materialbottomdrawer.draweritems.extra

import studio.forface.bottomappbar.materialbottomdrawer.holders.*

open class ButtonItem: SimpleExtraItem<ButtonItem>() {
    override val thisRef get() = this
    override fun newInstance() = ButtonItem()

    override var contentTextSizeHolder =            TextSizeHolder( sp = 12f )

    override var backgroundCornerRadiusSizeHolder = SizeHolder( dp = 6f )
}