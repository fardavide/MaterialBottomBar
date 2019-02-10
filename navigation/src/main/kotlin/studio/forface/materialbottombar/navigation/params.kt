package studio.forface.materialbottombar.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import studio.forface.materialbottombar.panels.params.Param

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that support Android's`Navigation Component`
 * Inherit from [Param]
 */
interface Navigation<T>: Param<T> {

    /** A [Bundle] for the destination of the navigation of the Item */
    var navDestinationBundle: Bundle?

    /** An [IdRes] for the destination of the navigation of the Item */
    @get: IdRes var navDestinationId: Int?

    /** A [NavDirections] for the navigation of the Item */
    var navDirections: NavDirections?

    /**
     * Set a [Bundle] for the destination of the item
     * @return [T]
     */
    fun navDestinationBundle( bundle: Bundle ) =
            thisRef.apply { navDestinationBundle = bundle }

    /**
     * Set an [IdRes] destination of the item
     * @return [T]
     */
    fun navDestinationId( @IdRes id: Int ) = thisRef.apply { navDestinationId = id }

    /**
     * Set a [NavDirections] for the item
     * @return [T]
     */
    fun navDirections( directions: NavDirections ) =
            thisRef.apply { navDirections = directions }
}

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that have a customizable [NavSelection]
 *
 * Inherit from [Param]
 */
interface NavSelection<T>: Param<T> {

    /** An [OnItemNavigationListener] for the Items */
    var onItemNavigation: OnItemNavigationListener

    /**
     * Apply the [OnItemNavigationListener] to the [NavSelection]
     * @return [T]
     */
    fun itemNavigationListener( onItemNavigationListener: OnItemNavigationListener ) =
            thisRef.apply { this@NavSelection.onItemNavigation = onItemNavigationListener }
}

/**
 * A typealias for a lambda that take an OPTIONAL [NavDestination] Id and an OPTIONAL [NavParams]
 * and return [Unit]
 */
internal typealias OnItemNavigationListener =
        ( directions: NavDirections?, navParams: NavParams? ) -> Unit

/** A typealias for a [Pair] of destinationId [Int] and an OPTIONAL bundle [Bundle] */
internal typealias NavParams = Pair<Int, Bundle?>

/** @return [NavItem.navDestinationId] from a [NavParams] */
internal val NavParams.destinationId get() = first

/** @return [NavItem.navDestinationBundle] from a [NavParams] */
internal val NavParams.bundle get() = second
