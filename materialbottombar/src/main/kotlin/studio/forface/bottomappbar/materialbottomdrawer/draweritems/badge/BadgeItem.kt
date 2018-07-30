package studio.forface.bottomappbar.materialbottomdrawer.draweritems.badge

import studio.forface.bottomappbar.materialbottomdrawer.holders.ColorHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.TextHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.TextSizeHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.TextStyleHolder
import studio.forface.bottomappbar.materialbottomdrawer.params.Background
import studio.forface.bottomappbar.materialbottomdrawer.params.Title

open class BadgeItem: Title<BadgeItem>, Background<BadgeItem> {
    override val thisRef get() = this

    override var titleTextHolder =          TextHolder()
    override var titleTextStyleHolder =     TextStyleHolder()
    override var titleTextSizeHolder =      TextSizeHolder()
    override var titleColorHolder =         ColorHolder()

    override var backgroundColorHolder =    ColorHolder()
}