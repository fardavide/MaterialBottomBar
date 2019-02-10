package studio.forface.materialbottombar.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import studio.forface.materialbottombar.drawer.MaterialDrawer
import studio.forface.materialbottombar.panels.MaterialPanel

/** An interface for Panels that implement Android's `Navigation` */
interface NavigationPanel {
    /**
     * A reference to the old [MaterialNavPanel.Body] for call
     * [MaterialNavPanel.Body.removeNavListener] when the body is changed.
     */
    var oldNavBody: MaterialNavPanel.Body?

    /** A reference to [MaterialPanel.body] for abstract [navController] behaviors */
    val navBody: MaterialNavPanel.Body?

    /** A reference to [MaterialPanel.body]'s [NavController] */
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
 * A [MaterialPanel] that implements Android's `Navigation`
 * Implements [NavigationPanel]
 */
class MaterialNavPanel(
        header: MaterialPanel.IHeader? = null,
        body: MaterialNavPanel.Body? = null,
        wrapToContent: Boolean = true
): MaterialPanel( header, body, wrapToContent,0 ), NavigationPanel {

    init {
        observe { _, change -> if ( change == Change.BODY ) updateNavListeners() }
    }

    /** @see NavigationPanel.navBody */
    override val navBody get() = body as MaterialNavPanel.Body?

    /** @see NavigationPanel.oldNavBody */
    override var oldNavBody: MaterialNavPanel.Body? = null

    /** An implementation of [MaterialPanel.BaseBody] that implements Android's `Navigation` */
    class Body( items: List<NavItem> = listOf() ): BaseBody<Body>( items ), NavSelection<Body> {
        override val thisRef: Body get() = this

        /** @see NavSelection.onItemNavigation */
        override var onItemNavigation: OnItemNavigationListener = { directions, navParams ->
            navController?.let { controller ->
                if ( directions != null ) controller.navigate( directions )
                else if ( navParams != null )
                    controller.navigate( navParams.destinationId, navParams.bundle )
            }
        }

        /** A reference to a [NavController] for handle Navigation */
        internal var navController: NavController? = null
            set(value) {
                field = value
                addNavListener()
            }

        /** A change listener for [navController] */
        private var navChangeListener: NavChange = { _, destination, _ ->
            setSelected { ( it as NavItem ).navDestinationId == destination.id }
        }

        /** Add [navChangeListener] to [navController] */
        internal fun addNavListener() {
            navController?.addOnDestinationChangedListener( navChangeListener )
        }

        /** Remove [navChangeListener] from [navController] */
        internal fun removeNavListener() {
            navController?.removeOnDestinationChangedListener( navChangeListener )
        }
    }
}

/**
 * A [MaterialDrawer] that implements Android's `Navigation`
 * Implements [NavigationPanel]
 */
class MaterialNavDrawer(
        header: MaterialPanel.IHeader? = null,
        body: MaterialNavPanel.Body? = null,
        wrapToContent: Boolean = false
): MaterialDrawer( header, body, wrapToContent,0 ), NavigationPanel {

    init {
        observe { _, change -> if ( change == Change.BODY ) updateNavListeners() }
    }

    /** @see NavigationPanel.navBody */
    override val navBody get() = body as MaterialNavPanel.Body?

    /** @see NavigationPanel.oldNavBody */
    override var oldNavBody: MaterialNavPanel.Body? = null
}

/**
 * A typealias for a lambda that receive a [NavController], a [NavDestination], and OPTIONAL
 * [Bundle] and return [Unit]
 */
private typealias NavChange = (NavController, NavDestination, Bundle?) -> Unit