package studio.forface.materialbottombar.navigation.adapter

import android.os.Handler
import android.view.View
import androidx.core.os.postDelayed
import studio.forface.materialbottombar.navigation.AbsMaterialNavPanel
import studio.forface.materialbottombar.navigation.NavItem
import studio.forface.materialbottombar.navigation.NavParams
import studio.forface.materialbottombar.panels.AbsMaterialPanel.BaseBody.Companion.SELECTION_DELAY_MS
import studio.forface.materialbottombar.panels.adapter.ItemViewHolder
import studio.forface.materialbottombar.panels.items.BasePanelItem

/**
 * @author Davide Giuseppe Farella
 * An [ItemViewHolder] that doesn't trigger the selection, but delivers
 * [AbsMaterialNavPanel.Body.onItemNavigation]
 */
class NavItemViewHolder(
        itemView: View,
        panelBody: AbsMaterialNavPanel.Body,
        closePanel: () -> Unit
): ItemViewHolder<AbsMaterialNavPanel.Body>( itemView, panelBody, closePanel ) {

    /** @see ItemViewHolder.itemClickListener */
    override val itemClickListener: (BasePanelItem<*>) -> (View) -> Unit get() = { item -> {
        handleBaseClicks( item )
        // Close Panel with a delay for let the ripple animation finish first
        Handler().postDelayed( SELECTION_DELAY_MS ) {
            if ( panelBody.closeOnClick ) closePanel()
        }
        // If item is NavItem, try to trigger the navigation callback
        if ( item is NavItem<*> ) itemNavigation( item )
    } }

    /** Handle the navigation of [NavItem] */
    private val itemNavigation: (NavItem<*>) -> Unit get() = { item ->
        // Create NavParams from item.navDestinationId if not null, with its bundle, if null
        // create from item.navDirections
        val navParams = item.navDestinationId?.let { NavParams( it, item.navDestinationBundle ) }
                ?: item.navDirections?.let { NavParams( it.actionId, it.arguments ) }

        // If navParams is not null, trigger the callback
        navParams?.let { panelBody.onItemNavigation( navParams ) }
    }
}