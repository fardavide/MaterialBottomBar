@file:Suppress("unused")

package studio.forface.bottomappbar.drawer

import android.view.View
import studio.forface.bottomappbar.appbar.MaterialBottomAppBar
import studio.forface.bottomappbar.panels.MaterialPanel
import studio.forface.bottomappbar.panels.items.PanelItem
import java.util.*

/**
 * @author Davide Giuseppe Farella.
 * A Drawer for a [MaterialBottomAppBar]
 *
 * Inherit from [MaterialPanel] ( inherit from [Observable] )
 */
class MaterialDrawer(
    _header: IHeader? = null,
    _body: IBody? = null,
    _wrapToContent: Boolean = false
): MaterialPanel( _header, _body, _wrapToContent ) {

    /** A solid implementation of [MaterialPanel.BaseHeader] */
    class Header: MaterialPanel.BaseHeader<Header>() {
        override val thisRef: Header get() = this
    }

    /**
     * A type of header that has a custom [View] as content.
     * Inherit from [MaterialPanel.CustomHeader] ( inherit from [Observable] )
     */
    class CustomHeader( _contentView: View ): MaterialPanel.CustomHeader( _contentView )

    /** A solid implementation of [MaterialPanel.BaseBody] */
    class Body( _items: List<PanelItem> = listOf() ): MaterialPanel.BaseBody<Body>( _items ) {
        override val thisRef: Body get() = this
    }

    /**
     * A type of body that has a custom [View] as content.
     * Inherit from [MaterialPanel.CustomBody] ( inherit from [Observable] )
     */
    class CustomBody( _contentView: View): MaterialPanel.CustomBody( _contentView )
}