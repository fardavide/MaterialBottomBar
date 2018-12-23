package studio.forface.bottomappbar.dsl

import studio.forface.bottomappbar.panels.MaterialPanel

/*
 * @Author: Davide Giuseppe Farella
 * A file containing dsl
 */

/** Crate a [MaterialPanel] via dsl */
inline fun panel( wrapToContent: Boolean = true, f: PanelBuilder.() -> Unit ): MaterialPanel {
    val builder = PanelBuilder( wrapToContent )
    builder.f()
    return builder.build()
}