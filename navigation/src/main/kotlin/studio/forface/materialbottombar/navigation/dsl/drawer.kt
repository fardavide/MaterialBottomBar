package studio.forface.materialbottombar.navigation.dsl

import androidx.navigation.NavController
import studio.forface.materialbottombar.navigation.MaterialNavDrawer

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

/** An [AbsNavPanelBuilder] for create a [MaterialNavDrawer] */
class NavDrawerBuilder(
        private val navController: NavController?,
        wrapToContent: Boolean
): AbsNavPanelBuilder<MaterialNavDrawer>( wrapToContent ) {

    /**
     * @return [MaterialNavDrawer]
     * @throws UninitializedPropertyAccessException if [_header] or [_body] has not been created
     */
    override fun build() = MaterialNavDrawer( _header, _body, wrapToContent )
            .apply { navController = this@NavDrawerBuilder.navController }
}