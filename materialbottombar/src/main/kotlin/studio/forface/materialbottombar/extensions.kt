@file:Suppress("unused")

package studio.forface.materialbottombar

import studio.forface.materialbottombar.dsl.MaterialPanel
import studio.forface.materialbottombar.layout.MaterialBottomDrawerLayout
import studio.forface.materialbottombar.layout.MaterialBottomDrawerLayout.Fly
import studio.forface.materialbottombar.layout.PanelChangeListener
import studio.forface.materialbottombar.layout.PanelStateChangeListener

/*
 * Author: Davide Giuseppe Farella
 * A set of extension function for support a more idiomatic code-style
 */

/** @see MaterialBottomDrawerLayout.setPanel */
operator fun MaterialBottomDrawerLayout.set( id: Int, materialPanel: MaterialPanel ) {
    setPanel( id, materialPanel )
}

/**
 * Execute a [callback] when a [MaterialPanel] changes its [Fly] state
 * It sets [MaterialBottomDrawerLayout.panelStateChangeListener]
 *
 * @param once if `true` reset the [MaterialBottomDrawerLayout.panelStateChangeListener] after the
 * first invocation
 * Default is `false`
 */
fun MaterialBottomDrawerLayout.doOnPanelState(
        once: Boolean = false,
        callback: PanelStateChangeListener
) {
    panelStateChangeListener = { id, state ->
        callback( id, state )
        if ( once ) panelStateChangeListener = { _, _ -> }
    }
}

/**
 * Execute a [callback] when a [MaterialPanel] changes its [Fly] state from [Fly.BOTTOM]
 * It sets [MaterialBottomDrawerLayout.panelOpenChangeListener]
 *
 * @param once if `true` reset the [MaterialBottomDrawerLayout.panelOpenChangeListener] after the
 * first invocation
 * Default is `false`
 */
fun MaterialBottomDrawerLayout.doOnPanelOpen(
        once: Boolean = false,
        callback: PanelChangeListener
) {
    panelOpenChangeListener = {
        callback( it )
        if ( once ) panelOpenChangeListener = {}
    }
}

/**
 * Execute a [callback] when a [MaterialPanel] changes its [Fly] state to [Fly.BOTTOM]
 * It sets [MaterialBottomDrawerLayout.panelCloseChangeListener]
 *
 * @param once if `true` reset the [MaterialBottomDrawerLayout.panelCloseChangeListener] after the
 * first invocation
 * Default is `false`
 */
fun MaterialBottomDrawerLayout.doOnPanelClose(
        once: Boolean = false,
        callback: PanelChangeListener
) {
    panelCloseChangeListener = {
        callback( it )
        if ( once ) panelCloseChangeListener = {}
    }
}