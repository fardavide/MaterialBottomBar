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
 */
fun MaterialBottomDrawerLayout.doOnPanelState( callback: PanelStateChangeListener ) {
    panelStateChangeListener = callback
}

/**
 * Execute a [callback] when a [MaterialPanel] changes its [Fly] state from [Fly.BOTTOM]
 * It sets [MaterialBottomDrawerLayout.panelOpenChangeListener]
 */
fun MaterialBottomDrawerLayout.doOnPanelOpen( callback: PanelChangeListener ) {
    panelOpenChangeListener = callback
}

/**
 * Execute a [callback] when a [MaterialPanel] changes its [Fly] state to [Fly.BOTTOM]
 * It sets [MaterialBottomDrawerLayout.panelCloseChangeListener]
 */
fun MaterialBottomDrawerLayout.doOnPanelClose( callback: PanelChangeListener ) {
    panelCloseChangeListener = callback
}