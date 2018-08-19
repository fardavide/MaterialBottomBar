package studio.forface.bottomappbar.drawer.items

import studio.forface.bottomappbar.panels.items.BasePanelItem

abstract class BaseDrawerItem: BasePanelItem(), DrawerItem {
    override val thisRef get() = this
}