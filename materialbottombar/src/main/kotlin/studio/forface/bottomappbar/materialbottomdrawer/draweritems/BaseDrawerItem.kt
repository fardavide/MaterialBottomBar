package studio.forface.bottomappbar.materialbottomdrawer.draweritems

import studio.forface.bottomappbar.materialbottomdrawer.draweritems.badge.BadgeItem
import studio.forface.bottomappbar.materialbottomdrawer.holders.*
import studio.forface.bottomappbar.materialbottomdrawer.params.Badge
import studio.forface.bottomappbar.materialbottomdrawer.params.Icon
import studio.forface.bottomappbar.materialbottomdrawer.params.Identifier
import studio.forface.bottomappbar.materialbottomdrawer.params.Title

abstract class BaseDrawerItem: DrawerItem,
        Title<BaseDrawerItem>,
        Icon<BaseDrawerItem>,
        Identifier<BaseDrawerItem>,
        Badge<BaseDrawerItem>
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
    override var iconSizeHolder =       IconSizeHolder()

    override var id = Int.MIN_VALUE

    var selectable = true
    var selected = false

    fun withSelectable( selectable: Boolean = true ) =
            thisRef.apply { this.selectable = selectable }

    override var badgeContentTextHolder =              TextHolder()
    override var badgeContentTextStyleHolder =         TextStyleHolder()
    override var badgeContentTextSizeHolder =          TextSizeHolder()
    override var badgeContentColorHolder =         ColorHolder()
    override var badgeBackgroundColorHolder =   ColorHolder()

    fun withBadgeItem( badgeItem: BadgeItem ) {
        badgeContentTextHolder =        badgeItem.titleTextHolder
        badgeContentTextStyleHolder =   badgeItem.titleTextStyleHolder
        badgeContentTextStyleHolder =   badgeItem.titleTextStyleHolder
        badgeContentColorHolder =       badgeItem.titleColorHolder
        badgeBackgroundColorHolder =    badgeItem.backgroundColorHolder
    }
}