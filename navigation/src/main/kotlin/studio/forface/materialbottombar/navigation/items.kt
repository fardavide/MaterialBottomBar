@file:Suppress("unused")

package studio.forface.materialbottombar.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import studio.forface.materialbottombar.panels.items.*

/** An interface of [PanelItem] that implements [Navigation] */
interface NavItem<T: PanelItem>: PanelItem, Navigation<T>

/** A [PrimaryPanelItem] that implements [NavItem] */
open class PrimaryNavPanelItem: AbsPrimaryPanelItem<PrimaryNavPanelItem>(), NavItem<PrimaryNavPanelItem> {
    override val thisRef get() = this
    override var navDirections: NavDirections? = null
    override var navDestinationBundle: Bundle? = null
    override var navDestinationId: Int? = null

    override fun clone(): PrimaryNavPanelItem {
        val item = super.clone()
        item.navDirections = navDirections
        item.navDestinationBundle = Bundle( navDestinationBundle )
        item.navDestinationId = navDestinationId
        return item
    }
}

/** A [PrimaryNavDrawerItem] that implements [NavItem]. Inherit from [PrimaryNavPanelItem] */
open class PrimaryNavDrawerItem: PrimaryNavPanelItem() {
    override val thisRef get() = this
}

/** A [SecondaryPanelItem] that implements [NavItem] */
open class SecondaryNavPanelItem: AbsSecondaryPanelItem<SecondaryNavPanelItem>(), NavItem<SecondaryNavPanelItem> {
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

/** A [SecondaryNavDrawerItem] that implements [NavItem]. Inherit from [SecondaryNavPanelItem] */
open class SecondaryNavDrawerItem: SecondaryNavPanelItem() {
    override val thisRef get() = this
}
