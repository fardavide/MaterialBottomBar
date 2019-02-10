@file:Suppress("unused")

package studio.forface.materialbottombar.navigation

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import studio.forface.bottomappbar.layout.MaterialBottomDrawerLayout

/*
 * Author: Davide Giuseppe Farella
 * A set of extension for Android *Navigation Components*
 */

/**
 * Setup with the given [NavController], [MaterialBottomDrawerLayout.bottomAppBar] and
 * [MaterialBottomDrawerLayout.panels]
 */
fun MaterialBottomDrawerLayout.setupWithNavController( controller: NavController ) {
    val navPanels = panels.values.mapNotNull { it as? NavigationPanel }.toTypedArray()
    bottomAppBar?.setupWithNavController( controller, *navPanels )
}

/** @see Toolbar.setupWithNavController and set the [NavController] to the given [materialPanels] */
fun Toolbar.setupWithNavController(
        controller: NavController,
        vararg materialPanels: NavigationPanel
) {
    setupWithNavController( controller,null )
    materialPanels.forEach { it.navController = controller }
}

/** @see Toolbar.setupWithNavController and set the [NavController] to the given [materialDrawer] */
fun Toolbar.setupWithNavController( controller: NavController, materialDrawer: MaterialNavDrawer ) {
    setupWithNavController( controller, materialDrawer )
}

