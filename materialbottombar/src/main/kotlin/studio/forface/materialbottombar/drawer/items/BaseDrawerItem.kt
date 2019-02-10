package studio.forface.materialbottombar.drawer.items

import studio.forface.materialbottombar.panels.items.BasePanelItem

abstract class BaseDrawerItem: BasePanelItem(), DrawerItem {
    override val thisRef get() = this
}