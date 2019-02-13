package studio.forface.materialbottombar.panels.items

import studio.forface.materialbottombar.panels.items.extra.BadgeItem
import studio.forface.materialbottombar.panels.items.extra.ButtonItem
import studio.forface.materialbottombar.panels.holders.*
import studio.forface.materialbottombar.panels.params.*
import kotlin.random.Random

abstract class BasePanelItem<T: PanelItem>: PanelItem,
        Badge<T>,
        Clickable<T>,
        Icon<T>,
        Identifier<T>,
        PanelButton<T>,
        Title<T>,
        Cloneable
{
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

    override var onClick: (T) -> Unit = {}
    override var id = Random.nextInt()

    var selectable = true
    var selected = false

    fun selectable( selectable: Boolean = true ) =
            apply { this.selectable = selectable }

    override var badgeItem =  BadgeItem()
    override var buttonItem = ButtonItem()
    override var buttonStyle = ButtonStyle.COLOR
}