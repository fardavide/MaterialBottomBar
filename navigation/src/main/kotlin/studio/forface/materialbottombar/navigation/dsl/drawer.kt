package studio.forface.materialbottombar.navigation.dsl

import androidx.navigation.NavController
import studio.forface.materialbottombar.navigation.AbsMaterialNavPanel

/**
 * @author Davide Giuseppe Farella
 * Crate a [MaterialNavDrawer] via dsl
 *
 * @see NavDrawerBuilder
 */
@Suppress("unused")
inline fun navDrawer(
        navController: NavController? = null,
        wrapToContent: Boolean = false,
        f: NavDrawerBuilder.() -> Unit
): MaterialNavDrawer {
    val builder = NavDrawerBuilder( navController, wrapToContent )
    builder.f()
    return builder.build()
}

/** A typealias of [NavPanelBuilder] for Drawer */
typealias NavDrawerBuilder = NavPanelBuilder

/** A typealias of [AbsMaterialNavPanel] for Drawer */
typealias MaterialNavDrawer = AbsMaterialNavPanel