package studio.forface.materialbottombar.navigation.dsl

import studio.forface.bottomappbar.dsl.AbsPanelBuilder
import studio.forface.bottomappbar.panels.MaterialPanel
import studio.forface.materialbottombar.navigation.MaterialNavPanel
import studio.forface.materialbottombar.navigation.MaterialNavPanel.Body
import studio.forface.materialbottombar.navigation.PrimaryNavPanelItem
import studio.forface.materialbottombar.navigation.SecondaryNavPanelItem

/**
 * @author Davide Giuseppe Farella.
 * A builder for create a [MaterialNavPanel] via dsl
 *
 * @param wrapToContent whether the panel, when opened, should have the same height of the children.
 * If false, the expanded panel, will have an height equal to 2/3 of the Window.
 *
 * Inherit from [AbsPanelBuilder]
 */
abstract class AbsNavPanelBuilder<T: MaterialPanel> internal constructor(
        wrapToContent: Boolean
): AbsPanelBuilder<
        T,
        MaterialNavPanel.Body,
        PrimaryNavPanelItem,
        SecondaryNavPanelItem
>( wrapToContent ) {

    /** Create a [Body] for the [MaterialPanel] */
    fun body( f: Body.() -> Unit ) {
        val body = Body()
        body.f()
        _body = body
    }
}