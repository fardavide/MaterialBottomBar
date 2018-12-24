package studio.forface.bottomappbar.dsl

import studio.forface.bottomappbar.drawer.MaterialDrawer

/**
 * @author Davide Giuseppe Farella
 * Crate a [MaterialDrawer] via dsl
 *
 * @see DrawerBuilder
 */
inline fun drawer( wrapToContent: Boolean = false, f: DrawerBuilder.() -> Unit ): MaterialDrawer {
    val builder = DrawerBuilder( wrapToContent )
    builder.f()
    return builder.build()
}

/** An [AbsPanelBuilder] for create a [MaterialDrawer] */
class DrawerBuilder( wrapToContent: Boolean ): AbsPanelBuilder<MaterialDrawer>( wrapToContent ) {

    /**
     * @see DslBuilder.build
     * @throws UninitializedPropertyAccessException if [_header] or [_body] has not been created
     */
    override fun build() = MaterialDrawer( _header, _body, wrapToContent )
}