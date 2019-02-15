package studio.forface.materialbottombar

import studio.forface.materialbottombar.dsl.MaterialPanel
import studio.forface.materialbottombar.layout.MaterialBottomDrawerLayout

/*
 * Author: Davide Giuseppe Farella
 * A set of extension function for support a more idiomatic code-style
 */

/** @see MaterialBottomDrawerLayout.setPanel */
operator fun MaterialBottomDrawerLayout.set( id: Int, materialPanel: MaterialPanel ) {
    setPanel( id, materialPanel )
}