package studio.forface.bottomappbar.dsl

import android.view.View
import studio.forface.bottomappbar.drawer.MaterialDrawer
import studio.forface.bottomappbar.panels.MaterialPanel
import studio.forface.bottomappbar.panels.MaterialPanel.IBody
import studio.forface.bottomappbar.panels.MaterialPanel.Body
import studio.forface.bottomappbar.panels.MaterialPanel.CustomBody
import studio.forface.bottomappbar.panels.MaterialPanel.Header
import studio.forface.bottomappbar.panels.MaterialPanel.CustomHeader

/**
 * @author Davide Giuseppe Farella.
 * A builder for create a [MaterialPanel] via dsl
 *
 * Inherit from [DslBuilder]
 */
class PanelBuilder( private val wrapToContent: Boolean ): DslBuilder<MaterialPanel> {

    private lateinit var _body: MaterialPanel.IBody
    private lateinit var _header: MaterialPanel.IHeader

    fun body( f: Body.() -> Unit ) {
        val body = Body()
        body.f()
        _body = body
    }

    fun customBody( contentView: View, f: CustomBody.() -> Unit ) {
        val body = CustomBody( contentView )
        body.f()
        _body = body
    }

    fun header( f: Header.() -> Unit ) {
        val header = Header()
        header.f()
        _header = header
    }

    fun customHeader( contentView: View, f: CustomHeader.() -> Unit ) {
        val header = CustomHeader( contentView )
        header.f()
        _header = header
    }

    /** @see DslBuilder.build */
    override fun build() = MaterialPanel( _header, _body, wrapToContent )
}