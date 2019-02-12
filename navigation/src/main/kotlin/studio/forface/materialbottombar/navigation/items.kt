@file:Suppress("unused")

package studio.forface.materialbottombar.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import studio.forface.materialbottombar.panels.items.*
import studio.forface.materialbottombar.panels.params.Identifier

/** An interface of [PanelItem] that implements [Navigation] */
interface NavItem<T: PanelItem>: PanelItem, Navigation<T>

/** A [PrimaryPanelItem] that implements [NavItem] */
class PrimaryNavPanelItem:
        AbsPrimaryPanelItem<PrimaryNavPanelItem>(), NavItem<PrimaryNavPanelItem> {
    override val thisRef get() = this
    override var navDirections: NavDirections? = null
    override var navDestinationBundle: Bundle? = null
    override var navDestinationId: Int? = null

    override fun clone(): PrimaryNavPanelItem {
        val item = super.clone()
        item.navDirections = navDirections
        item.navDestinationBundle = navDestinationBundle?.let { Bundle( it ) }
        item.navDestinationId = navDestinationId
        return item
    }
}

/** A typealias of [PrimaryNavPanelItem] for Drawer */
typealias PrimaryNavDrawerItem = PrimaryNavPanelItem

/** A [SecondaryPanelItem] that implements [NavItem] */
class SecondaryNavPanelItem:
        AbsSecondaryPanelItem<SecondaryNavPanelItem>(), NavItem<SecondaryNavPanelItem> {
    override val thisRef get() = this
    override var navDirections: NavDirections? = null
    override var navDestinationBundle: Bundle? = null
    override var navDestinationId: Int? = null

    override fun clone(): SecondaryNavPanelItem {
        val item = super.clone()
        item.navDirections = navDirections
        item.navDestinationBundle = Bundle( navDestinationBundle )
        item.navDestinationId = navDestinationId
        return item
    }
}

/** A typealias of [SecondaryNavPanelItem] for Drawer */
typealias SecondaryNavDrawerItem = SecondaryNavPanelItem