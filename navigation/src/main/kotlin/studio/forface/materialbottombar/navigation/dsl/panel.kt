package studio.forface.materialbottombar.navigation.dsl

import androidx.navigation.NavController
import studio.forface.materialbottombar.navigation.MaterialNavPanel

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

/** An [AbsNavPanelBuilder] for create a [MaterialNavPanel] */
class NavPanelBuilder(
        private val navController: NavController?,
        wrapToContent: Boolean
): AbsNavPanelBuilder<MaterialNavPanel>( wrapToContent ) {

    /**
     * @return [MaterialNavPanel]
     * @throws UninitializedPropertyAccessException if [_header] or [_body] has not been created
     */
    override fun build() = MaterialNavPanel( _header, _body, wrapToContent )
            .apply { navController = this@NavPanelBuilder.navController }
}