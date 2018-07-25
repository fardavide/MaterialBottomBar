package studio.forface.bottomappbar.materialbottomdrawer.draweritems

import studio.forface.bottomappbar.materialbottomdrawer.drawer.MaterialDrawer

data class PrimaryDrawerItem(
    val icon:   MaterialDrawer.Icon? =  null,
    val title:  MaterialDrawer.Title? = null
): DrawerItem