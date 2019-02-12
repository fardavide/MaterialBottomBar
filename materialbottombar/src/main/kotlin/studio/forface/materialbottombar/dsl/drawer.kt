package studio.forface.materialbottombar.dsl

import studio.forface.materialbottombar.panels.AbsMaterialPanel

/**
 * @author Davide Giuseppe Farella
 * Crate a [MaterialDrawer] via dsl
 *
 * @see CustomBodyPanelBuilder
 */
inline fun drawer( wrapToContent: Boolean = false, f: DrawerBuilder.() -> Unit ): MaterialDrawer {
    val builder = DrawerBuilder( wrapToContent )
    builder.f()
    return builder.build()
}

/** A typealias of [AbsPanelBuilder] for Drawer */
typealias DrawerBuilder = CustomBodyPanelBuilder

/** A typealias of [AbsMaterialPanel] for Drawer */
typealias MaterialDrawer = AbsMaterialPanel