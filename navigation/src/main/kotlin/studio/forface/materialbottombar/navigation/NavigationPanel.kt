@file:Suppress("FunctionName", "unused")

package studio.forface.materialbottombar.navigation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.os.postDelayed
import androidx.navigation.*
import studio.forface.materialbottombar.navigation.adapter.NavItemViewHolder
import studio.forface.materialbottombar.panels.AbsMaterialPanel
import studio.forface.materialbottombar.panels.adapter.ItemViewHolder
import studio.forface.materialbottombar.panels.items.BasePanelItem

/** An interface for Panels that implement Android's `Navigation` */
interface NavigationPanel {
    /**
     * A reference to the old [AbsMaterialNavPanel.Body] for call
     * [AbsMaterialNavPanel.Body.removeNavListener] when the body is changed.
     */
    var oldNavBody: AbsMaterialNavPanel.Body?

    /** A reference to [AbsMaterialPanel.body] for abstract [navController] behaviors */
    val navBody: AbsMaterialNavPanel.Body?

    /** A reference to [AbsMaterialPanel.body]'s [NavController] */
    var navController: NavController?
        get() = navBody?.navController
        set( value ) { navBody?.navController = value }

    /** Remove navigation listener from [oldNavBody] and add to [navBody] */
    fun updateNavListeners() {
        oldNavBody?.removeNavListener()
        navBody?.addNavListener()
        oldNavBody = navBody
    }
}

/**
 * An [AbsMaterialPanel] that implements Android's `Navigation`
 * Implements [NavigationPanel]
 */
abstract class AbsMaterialNavPanel(
        header: IHeader?,
        body: Body?,
        wrapToContent: Boolean
): AbsMaterialPanel( header, body, wrapToContent ), NavigationPanel {

    init {
        observe { _, change -> if ( change == Change.BODY ) updateNavListeners() }
    }

    /** @see NavigationPanel.navBody */
    override val navBody get() = body as Body?

    /** @see NavigationPanel.oldNavBody */
    override var oldNavBody: Body? = null

    /** An implementation of [AbsMaterialPanel.BaseBody] for [AbsMaterialNavPanel] */
    class Body(
            items: List<NavItem<*>> = listOf()
    ): AbsMaterialPanel.BaseBody<Body>( items ), NavSelection<Body> {
        override val thisRef: Body get() = this

        /** A change listener for [navController] */
        private var navChangeListener: NavChange = { _, destination, _ ->
            // Set the item selected with a delay for let the ripple animation finish first
            Handler().postDelayed( SELECTION_DELAY_MS ) {
                val thisItemId = destination.id
                setSelected {
                    val foundItemId = destination.actionDestinationIdOf( it )
                    thisItemId == foundItemId
                }
            }
        }

        /** A reference to a [NavController] for handle Navigation */
        internal var navController: NavController? = null
            set(value) {
                field = value
                addNavListener()
            }

        /** @see NavSelection.onItemNavigation */
        override var onItemNavigation: OnItemNavigationListener = { navParams ->
            navController?.let { controller ->
                val currentDestination = controller.currentDestination
                // If `null`, set 0 since `null` will depends by a `null` `currentDestination`, so
                // in that case `currentDestination?.id` would be equal to `nextDestinationId`
                val nextDestinationId = currentDestination?.actionDestinationIdOf( navParams ) ?: 0

                if ( currentDestination?.id != nextDestinationId )
                    controller.navigate( navParams.destinationId, navParams.bundle )
            }
        }

        /** Add [navChangeListener] to [navController] */
        internal fun addNavListener() {
            navController?.addOnDestinationChangedListener( navChangeListener )
        }

        /** @return a [NavItemViewHolder] for this [Body] */
        @Suppress("UNCHECKED_CAST")
        override fun <VH : ItemViewHolder<*>> createViewHolder(
                itemView: View, closePanel: () -> Unit
        ) = NavItemViewHolder( itemView,this, closePanel ) as VH

        /** Remove [navChangeListener] from [navController] */
        internal fun removeNavListener() {
            navController?.removeOnDestinationChangedListener( navChangeListener )
        }

        /** @see NavDestination.getAction */
        private fun NavDestination.actionDestinationIdOf( destinationId: Int ) =
                getAction( destinationId )?.destinationId

        /** @see NavDestination.getAction using a [NavParams] */
        private fun NavDestination.actionDestinationIdOf( navParams: NavParams ) =
                actionDestinationIdOf( navParams.destinationId )

        /** @see NavDestination.getAction using a [BasePanelItem] */
        private fun NavDestination.actionDestinationIdOf( item: BasePanelItem<*> ): Int? {
            val navItem = item as? NavItem<*>
            val navDirectionsId = navItem?.navDirections?.actionId?.let { actionDestinationIdOf( it ) }
            val navDestinationId = navItem?.navDestinationId?.let { actionDestinationIdOf( it ) }
            return navDirectionsId ?: navDestinationId
        }
    }
}

/**
 * A typealias for a lambda that receive a [NavController], a [NavDestination], and OPTIONAL
 * [Bundle] and return [Unit]
 */
private typealias NavChange = (NavController, NavDestination, Bundle?) -> Unit

/** @constructor of [AbsMaterialNavPanel] for Panel */
fun MaterialNavPanel(
        header: AbsMaterialPanel.IHeader? = null,
        body: AbsMaterialNavPanel.Body? = null,
        wrapToContent: Boolean = true
) = object : AbsMaterialNavPanel( header, body, wrapToContent ) {}

/** A set of constructors for Header and Body of [AbsMaterialNavPanel] for Panel */
object MaterialNavPanel {
    fun Header() = AbsMaterialPanel.Header()
    fun Body() = AbsMaterialNavPanel.Body()
    fun CustomHeader( customView: View ) = AbsMaterialPanel.CustomHeader( customView )
}

/** @constructor of [AbsMaterialNavPanel] for Drawer */
fun MaterialNavDrawer(
        header: AbsMaterialPanel.IHeader? = null,
        body: AbsMaterialNavPanel.Body? = null,
        wrapToContent: Boolean = false
) = object : AbsMaterialNavPanel( header, body, wrapToContent ) {}

/** A set of constructors for Header and Body of [AbsMaterialNavPanel] for Drawer */
object MaterialNavDrawer {
    fun Header() = AbsMaterialPanel.Header()
    fun Body() = AbsMaterialNavPanel.Body()
    fun CustomHeader( customView: View ) = AbsMaterialPanel.CustomHeader( customView )
}