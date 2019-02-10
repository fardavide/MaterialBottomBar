@file:Suppress("PropertyName", "MemberVisibilityCanBePrivate")

package studio.forface.materialbottombar.dsl

import android.view.View
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.annotation.StringRes
import studio.forface.materialbottombar.panels.MaterialPanel
import studio.forface.materialbottombar.panels.MaterialPanel.*
import studio.forface.materialbottombar.panels.items.Divider
import studio.forface.materialbottombar.panels.items.PrimaryPanelItem
import studio.forface.materialbottombar.panels.items.SecondaryPanelItem
import studio.forface.materialbottombar.panels.params.titleStringRes
import studio.forface.materialbottombar.panels.params.titleText

/**
 * @author Davide Giuseppe Farella.
 * A builder for create a [MaterialPanel] via dsl
 *
 * @param wrapToContent whether the panel, when opened, should have the same height of the children.
 * If false, the expanded panel, will have an height equal to 2/3 of the Window.
 *
 * Inherit from [DslBuilder]
 */
@Suppress("UNCHECKED_CAST")
abstract class AbsPanelBuilder<
        T: MaterialPanel,
        B: IBody,
        P: PrimaryPanelItem,
        S: SecondaryPanelItem
> @RestrictTo(LIBRARY_GROUP) constructor (
        @RestrictTo(LIBRARY_GROUP) val wrapToContent: Boolean
): DslBuilder<T> {

    /** A lateinit reference to the body of the [MaterialPanel] */
    @RestrictTo(LIBRARY_GROUP) lateinit var _body: B
    /** A lateinit reference to the header of the [MaterialPanel] */
    @RestrictTo(LIBRARY_GROUP) lateinit var _header: IHeader

    /** A base [PrimaryPanelItem] that will be used as base for every [Body.primaryItem] */
    @RestrictTo(LIBRARY_GROUP) var allPrimaryBodyItem = PrimaryPanelItem() as P
    /** A base [SecondaryPanelItem] that will be used as base for every [Body.secondaryItem] */
    @RestrictTo(LIBRARY_GROUP) var allSecondaryBodyItem = SecondaryPanelItem() as S

    /** A [DslComponent] for call [MaterialPanel.Body.setSelected] function */
    var Body.selectedItem: Int by dsl { setSelected( it ) }

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
    fun BaseBody<*>.allPrimary( f: P.() -> Unit ) {
        allPrimaryBodyItem.f()
    }

    /**
     * Crate a [SecondaryPanelItem] that will be used as base for every [Body.secondaryItem]
     * The [Body] receiver is useful for call this function only within a [Body], even if unused
     */
    @Suppress("unused")
    fun BaseBody<*>.allSecondary( f: S.() -> Unit ) {
        allSecondaryBodyItem.f()
    }

    /** Crate a [Divider] and add to the receiver [Body] */
    fun BaseBody<*>.divider() {
        items = items + Divider()
    }

    /** Crate a [PrimaryPanelItem] and add to the receiver [Body] */
    fun BaseBody<*>.primaryItem( f: P.() -> Unit ) {
        val item = allPrimaryBodyItem.clone() as P
        item.f()
        items = items + item
    }

    /** Crate a [PrimaryPanelItem] with the given [titleText] and add to the receiver [Body] */
    fun BaseBody<*>.primaryItem( titleText: CharSequence, f: P.() -> Unit ) {
        primaryItem {
            this.titleText = titleText
            f()
        }
    }

    /** Crate a [PrimaryPanelItem] with the given [titleStringRes] and add to the receiver [Body] */
    fun BaseBody<*>.primaryItem( @StringRes titleStringRes: Int, f: P.() -> Unit ) {
        primaryItem {
            this.titleStringRes = titleStringRes
            f()
        }
    }

    /** Crate a [SecondaryPanelItem] and add to the receiver [Body] */
    open fun BaseBody<*>.secondaryItem( f: S.() -> Unit ) {
        val item = allSecondaryBodyItem.clone() as S
        item.f()
        items = items + item
    }

    /** Crate a [SecondaryPanelItem] with the given [titleText] and add to the receiver [Body] */
    fun BaseBody<*>.secondaryItem( titleText: CharSequence, f: S.() -> Unit ) {
        secondaryItem {
            this.titleText = titleText
            f()
        }
    }

    /** Crate a [SecondaryPanelItem] with the given [titleStringRes] and add to the receiver [Body] */
    fun BaseBody<*>.secondaryItem( @StringRes titleStringRes: Int, f: S.() -> Unit ) {
        secondaryItem {
            this.titleStringRes = titleStringRes
            f()
        }
    }
}

/** An [AbsPanelBuilder] for [MaterialPanel]s that can have a [MaterialPanel.CustomBody] */
abstract class CustomBodyPanelBuilder<T: MaterialPanel> internal constructor(
        wrapToContent: Boolean
): AbsPanelBuilder<T, IBody, PrimaryPanelItem, SecondaryPanelItem>( wrapToContent ) {

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
}