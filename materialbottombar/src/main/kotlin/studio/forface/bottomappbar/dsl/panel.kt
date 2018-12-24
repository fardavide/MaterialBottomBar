package studio.forface.bottomappbar.dsl

import studio.forface.bottomappbar.panels.MaterialPanel

/**
 * @author Davide Giuseppe Farella
 * Crate a [MaterialPanel] via dsl
 *
 * @see PanelBuilder
 */
inline fun panel( wrapToContent: Boolean = true, f: PanelBuilder.() -> Unit ): MaterialPanel {
    val builder = PanelBuilder( wrapToContent )
    builder.f()
    return builder.build()
}

/** An [AbsPanelBuilder] for create a [MaterialPanel] */
class PanelBuilder( wrapToContent: Boolean ): AbsPanelBuilder<MaterialPanel>( wrapToContent ) {

    /**
     * @see DslBuilder.build
     * @throws UninitializedPropertyAccessException if [_header] or [_body] has not been created
     */
    override fun build() = MaterialPanel( _header, _body, wrapToContent )
}