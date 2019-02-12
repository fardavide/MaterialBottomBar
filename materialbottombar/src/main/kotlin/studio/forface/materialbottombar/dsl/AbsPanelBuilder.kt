@file:Suppress("PropertyName", "MemberVisibilityCanBePrivate")

package studio.forface.materialbottombar.dsl

import android.view.View
import androidx.annotation.StringRes
import studio.forface.materialbottombar.panels.AbsMaterialPanel
import studio.forface.materialbottombar.panels.AbsMaterialPanel.*
import studio.forface.materialbottombar.panels.MaterialPanel
import studio.forface.materialbottombar.panels.items.*
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
abstract class AbsPanelBuilder<
        B: IBody,
        P: AbsPrimaryPanelItem<P>,
        S: AbsSecondaryPanelItem<S>
> ( val wrapToContent: Boolean ): DslBuilder<AbsMaterialPanel> {

    /** A lateinit reference to the body of the [MaterialPanel] */
    lateinit var _body: B
    /** A lateinit reference to the header of the [MaterialPanel] */
    lateinit var _header: IHeader

    /** A base [PrimaryPanelItem] that will be used as base for every [Body.primaryItem] */
    open var allPrimaryBodyItem = PrimaryPanelItem() as P
    /** A base [SecondaryPanelItem] that will be used as base for every [Body.secondaryItem] */
    open var allSecondaryBodyItem = SecondaryPanelItem() as S

    /** A [DslComponent] for call [Body.setSelected] function */
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
    fun <B: BaseBody> B.allPrimary( f: P.() -> Unit ) {
        allPrimaryBodyItem.f()
    }

    /**
     * Crate a [SecondaryPanelItem] that will be used as base for every [Body.secondaryItem]
     * The [Body] receiver is useful for call this function only within a [Body], even if unused
     */
    @Suppress("unused")
    fun <B: BaseBody> B.allSecondary( f: S.() -> Unit ) {
        allSecondaryBodyItem.f()
    }

    /** Crate a [Divider] and add to the receiver [Body] */
    fun <B: BaseBody> B.divider() {
        items = items + Divider()
    }

    /** Crate a [PrimaryPanelItem] and add to the receiver [Body] */
    fun <B: BaseBody> B.primaryItem( f: P.() -> Unit ) {
        val item = allPrimaryBodyItem.clone()
        item.f()
        items = items + item
    }

    /** Crate a [PrimaryPanelItem] with the given [titleText] and add to the receiver [Body] */
    fun <B: BaseBody> B.primaryItem( titleText: CharSequence, f: P.() -> Unit ) {
        primaryItem {
            this.titleText = titleText
            f()
        }
    }

    /** Crate a [PrimaryPanelItem] with the given [titleStringRes] and add to the receiver [Body] */
    fun <B: BaseBody> B.primaryItem( @StringRes titleStringRes: Int, f: P.() -> Unit ) {
        primaryItem {
            this.titleStringRes = titleStringRes
            f()
        }
    }

    /** Crate a [SecondaryPanelItem] and add to the receiver [Body] */
    open fun <B: BaseBody> B.secondaryItem( f: S.() -> Unit ) {
        val item = allSecondaryBodyItem.clone()
        item.f()
        items = items + item
    }

    /** Crate a [SecondaryPanelItem] with the given [titleText] and add to the receiver [Body] */
    fun <B: BaseBody> B.secondaryItem( titleText: CharSequence, f: S.() -> Unit ) {
        secondaryItem {
            this.titleText = titleText
            f()
        }
    }

    /** Crate a [SecondaryPanelItem] with the given [titleStringRes] and add to the receiver [Body] */
    fun <B: BaseBody> B.secondaryItem( @StringRes titleStringRes: Int, f: S.() -> Unit ) {
        secondaryItem {
            this.titleStringRes = titleStringRes
            f()
        }
    }
}

typealias BaseBody = AbsMaterialPanel.BaseBody<*>

/** An [AbsPanelBuilder] for [MaterialPanel]s that can have a [MaterialPanel.CustomBody] */
class CustomBodyPanelBuilder @PublishedApi internal constructor(
        wrapToContent: Boolean
): AbsPanelBuilder<
        IBody,
        PrimaryPanelItem,
        SecondaryPanelItem
>( wrapToContent ) {

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

    /**
     * @see DslBuilder.build
     * @throws UninitializedPropertyAccessException if [_header] or [_body] has not been created
     */
    override fun build() = MaterialPanel( _header, _body, wrapToContent )
}