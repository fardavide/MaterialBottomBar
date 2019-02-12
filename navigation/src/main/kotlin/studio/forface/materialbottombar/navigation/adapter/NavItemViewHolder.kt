package studio.forface.materialbottombar.navigation.adapter

import android.view.View
import studio.forface.materialbottombar.navigation.AbsMaterialNavPanel
import studio.forface.materialbottombar.navigation.NavItem
import studio.forface.materialbottombar.navigation.NavParams
import studio.forface.materialbottombar.panels.adapter.ItemViewHolder
import studio.forface.materialbottombar.panels.items.BasePanelItem

/**
 * @author Davide Giuseppe Farella
 * An [ItemViewHolder] that doesn't trigger the selection, but delivers
 * [AbsMaterialNavPanel.Body.onItemNavigation]
 */
class NavItemViewHolder(
        itemView: View,
        panelBody: AbsMaterialNavPanel.Body
): ItemViewHolder<AbsMaterialNavPanel.Body>( itemView, panelBody ) {

    /** @see ItemViewHolder.itemClickListener */
    override val itemClickListener: (BasePanelItem<*>) -> (View) -> Unit get() = { item -> {
        panelBody.onItemClick( item.id, title )

        ( item as? NavItem<*> )?.let { itemNavigation( it ) }
    } }

    private val itemNavigation: (NavItem<*>) -> Unit get() = { item ->
        val navParams = item.navDestinationId?.let { NavParams( it, item.navDestinationBundle ) }
        panelBody.onItemNavigation( item.navDirections, navParams )
    }
}