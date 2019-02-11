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
    var oldNavBody: MaterialNavPanel.BaseBody<*>?

    /** A reference to [MaterialPanel.body] for abstract [navController] behaviors */
    val navBody: MaterialNavPanel.BaseBody<*>?

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
    override val navBody get() = body as MaterialNavPanel.BaseBody<*>?

    /** @see NavigationPanel.oldNavBody */
    override var oldNavBody: MaterialNavPanel.BaseBody<*>? = null

    /**
     * An abstract class that implements business logic of a base body.
     * This class is needed since if will be inherited from [MaterialNavDrawer.Body]
     *
     * Inherit from [MaterialPanel.BaseBody] and [NavSelection]
     */
    abstract class BaseBody<T>(
            items: List<NavItem<*>> = listOf()
    ): MaterialPanel.BaseBody<T>( items ), NavSelection<T> {

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
            setSelected { ( it as NavItem<*> ).navDestinationId == destination.id }
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

    /** A solid implementation of [MaterialNavPanel.BaseBody] */
    class Body( items: List<NavItem<*>> = listOf() ): BaseBody<Body>( items ) {
        override val thisRef: Body get() = this
    }
}

/**
 * A [MaterialDrawer] that implements Android's `Navigation`
 * Implements [NavigationPanel]
 */
class MaterialNavDrawer(
        header: MaterialPanel.IHeader? = null,
        body: MaterialNavPanel.BaseBody<*>? = null,
        wrapToContent: Boolean = false
): MaterialDrawer( header, body, wrapToContent,0 ), NavigationPanel {

    init {
        observe { _, change -> if ( change == Change.BODY ) updateNavListeners() }
    }

    /** @see NavigationPanel.navBody */
    override val navBody get() = body as MaterialNavPanel.BaseBody<*>?

    /** @see NavigationPanel.oldNavBody */
    override var oldNavBody: MaterialNavPanel.BaseBody<*>? = null

    /** A solid implementation of [MaterialNavPanel.BaseBody] */
    class Body( items: List<NavItem<*>> = listOf() ): MaterialNavPanel.BaseBody<Body>( items ) {
        override val thisRef: Body get() = this
    }
}

/**
 * A typealias for a lambda that receive a [NavController], a [NavDestination], and OPTIONAL
 * [Bundle] and return [Unit]
 */
private typealias NavChange = (NavController, NavDestination, Bundle?) -> Unit