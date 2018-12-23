package studio.forface.bottomappbar.drawer

import android.view.View
import studio.forface.bottomappbar.panels.MaterialPanel
import studio.forface.bottomappbar.panels.items.BasePanelItem
import studio.forface.bottomappbar.panels.items.PanelItem

class MaterialDrawer(
    _header: Header? = null,
    _body: IBody? = null,
    _wrapToContent: Boolean = false
): MaterialPanel( _header, _body, _wrapToContent ) {

    class Header: MaterialPanel.BaseHeader<Header>() {
        override val thisRef: Header get() = this
    }

    class CustomHeader( _contentView: View ): MaterialPanel.CustomBody( _contentView )

    class Body( _items: List<PanelItem> = listOf() ): MaterialPanel.BaseBody<Body>( _items ) {
        override val thisRef: Body get() = this
    }

    class CustomBody( _contentView: View): MaterialPanel.CustomBody( _contentView )
}

inline fun List<PanelItem>.mapBasePanelItems(mapper: (BasePanelItem) -> Unit ) =
        this.map { ( it as? BasePanelItem )?.apply { mapper( this ) } ?: it }

inline fun List<PanelItem>.forEachBasePanelItem(block: (BasePanelItem) -> Unit ) {
    forEach { ( it as? BasePanelItem )?.run( block ) }
}