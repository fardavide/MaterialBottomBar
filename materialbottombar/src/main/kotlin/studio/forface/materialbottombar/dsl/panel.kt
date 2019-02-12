package studio.forface.materialbottombar.dsl

import studio.forface.materialbottombar.panels.AbsMaterialPanel

/**
 * @author Davide Giuseppe Farella
 * Crate a [MaterialPanel] via dsl
 *
 * @see CustomBodyPanelBuilder
 */
inline fun panel( wrapToContent: Boolean = true, f: PanelBuilder.() -> Unit ): MaterialPanel {
    val builder = PanelBuilder( wrapToContent )
    builder.f()
    return builder.build()
}

/** A typealias of [AbsPanelBuilder] for Panel */
typealias PanelBuilder = CustomBodyPanelBuilder

/** A typealias of [AbsMaterialPanel] for Panel */
typealias MaterialPanel = AbsMaterialPanel