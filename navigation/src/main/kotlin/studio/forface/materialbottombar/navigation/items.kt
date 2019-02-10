@file:Suppress("unused")

package studio.forface.materialbottombar.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import studio.forface.bottomappbar.panels.items.BasePanelItem
import studio.forface.bottomappbar.panels.items.PanelItem
import studio.forface.bottomappbar.panels.items.PrimaryPanelItem
import studio.forface.bottomappbar.panels.items.SecondaryPanelItem

/** An interface of [PanelItem] that implements [Navigation] */
interface NavItem: PanelItem, Navigation<BasePanelItem>

/** A [PrimaryPanelItem] that implements [NavItem] */
open class PrimaryNavPanelItem: PrimaryPanelItem(), NavItem {
    override val thisRef get() = this
    override var navDirections: NavDirections? = null
    override var navDestinationBundle: Bundle? = null
    override var navDestinationId: Int? = null
}

/** A [PrimaryNavDrawerItem] that implements [NavItem]. Inherit from [PrimaryNavPanelItem] */
open class PrimaryNavDrawerItem: PrimaryNavPanelItem() {
    override val thisRef get() = this
}

/** A [SecondaryPanelItem] that implements [NavItem] */
open class SecondaryNavPanelItem: SecondaryPanelItem(), NavItem {
    override val thisRef get() = this
    override var navDirections: NavDirections? = null
    override var navDestinationBundle: Bundle? = null
    override var navDestinationId: Int? = null
}

/** A [SecondaryNavDrawerItem] that implements [NavItem]. Inherit from [SecondaryNavPanelItem] */
open class SecondaryNavDrawerItem: SecondaryNavPanelItem() {
    override val thisRef get() = this
}
