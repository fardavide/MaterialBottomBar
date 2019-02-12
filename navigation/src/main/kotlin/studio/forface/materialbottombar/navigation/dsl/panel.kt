package studio.forface.materialbottombar.navigation.dsl

import androidx.navigation.NavController
import studio.forface.materialbottombar.navigation.AbsMaterialNavPanel

/**
 * @author Davide Giuseppe Farella
 * Crate a [MaterialNavPanel] via dsl
 *
 * @see NavPanelBuilder
 */
@Suppress("unused")
inline fun navPanel(
        navController: NavController? = null,
        wrapToContent: Boolean = true,
        f: NavPanelBuilder.() -> Unit
): MaterialNavPanel {
    val builder = NavPanelBuilder( navController, wrapToContent )
    builder.f()
    return builder.build()
}

/** A typealias of [AbsMaterialNavPanel] for Panel */
typealias MaterialNavPanel = AbsMaterialNavPanel