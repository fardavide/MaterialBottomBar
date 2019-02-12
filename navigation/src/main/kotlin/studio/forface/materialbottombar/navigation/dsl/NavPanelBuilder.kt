package studio.forface.materialbottombar.navigation.dsl

import androidx.navigation.NavController
import studio.forface.materialbottombar.dsl.AbsPanelBuilder
import studio.forface.materialbottombar.navigation.AbsMaterialNavPanel
import studio.forface.materialbottombar.navigation.AbsMaterialNavPanel.Body
import studio.forface.materialbottombar.navigation.MaterialNavPanel
import studio.forface.materialbottombar.navigation.MaterialNavPanel.Body
import studio.forface.materialbottombar.navigation.PrimaryNavPanelItem
import studio.forface.materialbottombar.navigation.SecondaryNavPanelItem
import studio.forface.materialbottombar.panels.MaterialPanel
import studio.forface.materialbottombar.panels.items.PrimaryPanelItem
import studio.forface.materialbottombar.panels.items.SecondaryPanelItem

/**
 * @author Davide Giuseppe Farella.
 * A builder for create a [MaterialNavPanel] via dsl
 *
 * @param wrapToContent whether the panel, when opened, should have the same height of the children.
 * If false, the expanded panel, will have an height equal to 2/3 of the Window.
 *
 * Inherit from [AbsPanelBuilder]
 */
class NavPanelBuilder @PublishedApi internal constructor(
        private val navController: NavController?,
        wrapToContent: Boolean
): AbsPanelBuilder<
        AbsMaterialNavPanel.Body,
        PrimaryNavPanelItem,
        SecondaryNavPanelItem
>( wrapToContent ) {

    /** A base [PrimaryPanelItem] that will be used as base for every [Body.primaryItem] */
    override var allPrimaryBodyItem = PrimaryNavPanelItem()

    /** A base [SecondaryPanelItem] that will be used as base for every [Body.secondaryItem] */
    override var allSecondaryBodyItem = SecondaryNavPanelItem()

    /** Create a [Body] for the [MaterialPanel] */
    fun body( f: AbsMaterialNavPanel.Body.() -> Unit ) {
        val body = Body()
        body.f()
        _body = body
    }

    /**
     * @return [MaterialNavPanel]
     * @throws UninitializedPropertyAccessException if [_header] or [_body] has not been created
     */
    override fun build() = MaterialNavPanel( _header, _body, wrapToContent )
            .apply { navController = this@NavPanelBuilder.navController }
}