package studio.forface.bottomappbar.materialbottomdrawer.draweritems

import studio.forface.bottomappbar.materialpanels.panelitems.BasePanelItem

abstract class BaseDrawerItem: BasePanelItem(), DrawerItem {
    override val thisRef get() = this
}