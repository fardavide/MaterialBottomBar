package studio.forface.bottomappbar.materialbottomdrawer.draweritems

import studio.forface.bottomappbar.materialbottomdrawer.draweritems.extra.BadgeItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.extra.ButtonItem
import studio.forface.bottomappbar.materialbottomdrawer.holders.*
import studio.forface.bottomappbar.materialbottomdrawer.params.*

abstract class BaseDrawerItem: DrawerItem,
        Title<BaseDrawerItem>,
        Icon<BaseDrawerItem>,
        Identifier<BaseDrawerItem>,
        Badge<BaseDrawerItem>,
        DrawerButton<BaseDrawerItem>
{
    override val thisRef get() = this
    abstract val iconMarginStartDp: Float
    abstract val iconMarginEndDp: Float
    abstract val iconAlpha: Float

    override var titleTextHolder =      TextHolder()
    override var titleTextStyleHolder = TextStyleHolder()
    override var titleColorHolder =     ColorHolder()
    override var titleTextSizeHolder =  TextSizeHolder()

    override var iconImageHolder =      ImageHolder()
    override var iconColorHolder =      ColorHolder()
    override var iconSizeHolder =       SizeHolder()

    override var id = Int.MIN_VALUE

    var selectable = true
    var selected = false

    fun withSelectable( selectable: Boolean = true ) =
            apply { this.selectable = selectable }

    override var badgeItem =  BadgeItem()
    override var buttonItem = ButtonItem()
}