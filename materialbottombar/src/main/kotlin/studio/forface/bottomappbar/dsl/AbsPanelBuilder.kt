@file:Suppress("PropertyName", "MemberVisibilityCanBePrivate")

package studio.forface.bottomappbar.dsl

import android.view.View
import androidx.annotation.StringRes
import studio.forface.bottomappbar.panels.MaterialPanel
import studio.forface.bottomappbar.panels.MaterialPanel.*
import studio.forface.bottomappbar.panels.items.Divider
import studio.forface.bottomappbar.panels.items.PrimaryPanelItem
import studio.forface.bottomappbar.panels.items.SecondaryPanelItem
import studio.forface.bottomappbar.panels.params.titleStringRes
import studio.forface.bottomappbar.panels.params.titleText
import java.util.*

/**
 * @author Davide Giuseppe Farella.
 * A builder for create a [MaterialPanel] via dsl
 *
 * @param wrapToContent whether the panel, when opened, should have the same height of the children.
 * If false, the expanded panel, will have an height equal to 2/3 of the Window.
 *
 * Inherit from [DslBuilder]
 */
abstract class AbsPanelBuilder<T: MaterialPanel>( internal val wrapToContent: Boolean ): DslBuilder<T> {

    /** A lateinit reference to the body of the [MaterialPanel] */
    internal lateinit var _body: MaterialPanel.IBody
    /** A lateinit reference to the header of the [MaterialPanel] */
    internal lateinit var _header: MaterialPanel.IHeader

    /** A base [PrimaryPanelItem] that will be used as base for every [Body.primaryItem] */
    private var allPrimaryBodyItem = PrimaryPanelItem()
    /** A base [SecondaryPanelItem] that will be used as base for every [Body.secondaryItem] */
    private var allSecondaryBodyItem = SecondaryPanelItem()

    /** A [DslComponent] for call [MaterialPanel.Body.setSelected] function */
    var Body.selectedItem: Int by dsl { setSelected( it ) }

    /** Create a [Body] for the [MaterialPanel] */
    fun body( f: Body.() -> Unit ) {
        val body = Body()
        body.f()
        _body = body
    }

    /** Create a [CustomBody] for the [MaterialPanel] */
    fun customBody( contentView: View, f: CustomBody.() -> Unit ) {
        val body = CustomBody( contentView )
        body.f()
        _body = body
    }

    /** Create an [Header] for the [MaterialPanel] */
    fun header( f: Header.() -> Unit ) {
        val header = Header()
        header.f()
        _header = header
    }

    /** Create a [CustomHeader] for the [MaterialPanel] */
    fun customHeader( contentView: View, f: CustomHeader.() -> Unit ) {
        val header = CustomHeader(contentView)
        header.f()
        _header = header
    }


    /**
     * Crate a [PrimaryPanelItem] that will be used as base for every [Body.primaryItem]
     * The [Body] receiver is useful for call this function only within a [Body], even if unused
     */
    @Suppress("unused")
    fun Body.allPrimary(f: PrimaryPanelItem.() -> Unit ) {
        allPrimaryBodyItem.f()
    }

    /**
     * Crate a [SecondaryPanelItem] that will be used as base for every [Body.secondaryItem]
     * The [Body] receiver is useful for call this function only within a [Body], even if unused
     */
    @Suppress("unused")
    fun Body.allSecondary(f: SecondaryPanelItem.() -> Unit ) {
        allSecondaryBodyItem.f()
    }

    /** Crate a [Divider] and add to the receiver [Body] */
    fun Body.divider() {
        items += Divider()
    }

    /** Crate a [PrimaryPanelItem] and add to the receiver [Body] */
    fun Body.primaryItem( f: PrimaryPanelItem.() -> Unit ) {
        val item = allPrimaryBodyItem.clone()
        item.f()
        items += item
    }

    /** Crate a [PrimaryPanelItem] with the given [titleText] and add to the receiver [Body] */
    fun Body.primaryItem( titleText: CharSequence, f: PrimaryPanelItem.() -> Unit ) {
        val item = allPrimaryBodyItem.clone()
        item.titleText = titleText
        item.f()
        items += item
    }

    /** Crate a [PrimaryPanelItem] with the given [titleStringRes] and add to the receiver [Body] */
    fun Body.primaryItem( @StringRes titleStringRes: Int, f: PrimaryPanelItem.() -> Unit ) {
        val item = allPrimaryBodyItem.clone()
        item.titleStringRes = titleStringRes
        item.f()
        items += item
    }

    /** Crate a [SecondaryPanelItem] and add to the receiver [Body] */
    fun Body.secondaryItem( f: SecondaryPanelItem.() -> Unit ) {
        val item = allSecondaryBodyItem.clone()
        item.f()
        items += item
    }

    /** Crate a [SecondaryPanelItem] with the given [titleText] and add to the receiver [Body] */
    fun Body.secondaryItem( titleText: CharSequence, f: SecondaryPanelItem.() -> Unit ) {
        val item = allSecondaryBodyItem.clone()
        item.titleText = titleText
        item.f()
        items += item
    }

    /** Crate a [SecondaryPanelItem] with the given [titleStringRes] and add to the receiver [Body] */
    fun Body.secondaryItem( @StringRes titleStringRes: Int, f: SecondaryPanelItem.() -> Unit ) {
        val item = allSecondaryBodyItem.clone()
        item.titleStringRes = titleStringRes
        item.f()
        items += item
    }
}