package studio.forface.bottomappbar.panels.items.extra

import studio.forface.bottomappbar.panels.holders.*
import studio.forface.bottomappbar.panels.params.Param

/**
 * @author Davide Giuseppe Farella.
 * An extra Item for Badge.
 *
 * Inherit from [SimpleExtraItem]
 */
open class BadgeItem: SimpleExtraItem<BadgeItem>() {

    /** @see Param.thisRef */
    override val thisRef get() = this

    /** A reference to [SizeHolder] for the Background corner radius of the Badge */
    override var backgroundCornerRadiusSizeHolder = SizeHolder( dp = 6f )
    /** A reference to [TextSizeHolder] for the Content size of the Badge */
    override var contentTextSizeHolder = TextSizeHolder( sp = 9f )
    /** A reference to [TextStyleHolder] for the Content style of the Badge */
    override var contentTextStyleHolder = TextStyleHolder( bold = true )
}

/** A function for create a [BadgeItem] within a DSL style */
@Suppress("FunctionName")
fun Badge( f: BadgeItem.() -> Unit ): BadgeItem {
    val item = BadgeItem()
    item.f()
    return item
}