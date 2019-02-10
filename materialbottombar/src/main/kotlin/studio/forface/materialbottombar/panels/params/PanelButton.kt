@file:Suppress("unused")

package studio.forface.materialbottombar.panels.params

import android.text.Spannable
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import studio.forface.materialbottombar.dsl.DslComponent
import studio.forface.materialbottombar.dsl.dsl
import studio.forface.materialbottombar.panels.items.extra.ButtonItem

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that have a customizable [PanelButton]
 *
 * Inherit from [Param]
 */
interface PanelButton<T>: Param<T> {

    /** A reference to [ButtonItem] for the Button */
    var buttonItem: ButtonItem
    /** A reference to [ButtonStyle] for the Button */
    var buttonStyle: ButtonStyle

    /** Apply the [PanelButton] to a [Button] */
    fun applyButtonTo( button: Button ) {
        buttonItem.applyTo( button, buttonStyle )
    }

    /**
     * Set a new [ButtonItem]
     * @return [T]
     */
    fun buttonItem( button: ButtonItem ) = thisRef.apply { buttonItem = button.cloneRef() }

    /**
     * Apply the ID to the Identifier of [ButtonItem]
     * @see Identifier.id
     * @return [T]
     */
    fun buttonId( id: Int ) = thisRef.apply { buttonItem.id = id }

    /**
     * Apply the [ColorInt] to the Background of [ButtonItem]
     * @see Background.backgroundColor
     * @return [T]
     */
    fun buttonBackgroundColor( @ColorInt color: Int ) =
            thisRef.apply { buttonItem.backgroundColor( color ) }

    /**
     * Apply the [ColorRes] to the Background of [ButtonItem]
     * @see Background.backgroundColorRes
     * @return [T]
     */
    fun buttonBackgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { buttonItem.backgroundColorRes( res ) }

    /**
     * Apply the corner radius in DP size to the Background of [ButtonItem]
     * @see Background.backgroundCornerRadiusDp
     * @return [T]
     */
    fun buttonBackgroundCornerRadiusDp( dp: Float ) =
            thisRef.apply { buttonItem.backgroundCornerRadiusDp( dp ) }

    /**
     * Apply the corner radius in Pixel size to the Background of [ButtonItem]
     * @see Background.backgroundCornerRadiusPixel
     * @return [T]
     */
    fun buttonBackgroundCornerRadiusPixel( pixel: Float ) =
            thisRef.apply { buttonItem.backgroundCornerRadiusPixel( pixel ) }


    /**
     * Apply the size in DP to the Content of [ButtonItem]
     * @see Content.contentDpSize
     * @return [T]
     */
    fun buttonContentDpSize( size: Float ) =
            thisRef.apply { buttonItem.contentDpSize( size ) }

    /**
     * Apply the size in Pixel to the Content of [ButtonItem]
     * @see Content.contentPixelSize
     * @return [T]
     */
    fun buttonContentPixelSize( size: Float ) =
            thisRef.apply { buttonItem.contentPixelSize( size ) }

    /**
     * Apply the size in SP to the Content of [ButtonItem]
     * @see Content.contentSpSize
     * @return [T]
     */
    fun buttonContentSpSize( size: Float ) =
            thisRef.apply { buttonItem.contentSpSize( size ) }

    /**
     * Apply the bold to the Style of Content of [ButtonItem]
     * @see Content.contentBold
     * @return [T]
     */
    fun buttonContentBold( bold: Boolean = true ) =
            thisRef.apply { buttonItem.contentBold( bold ) }

    /**
     * Apply the [ColorInt] to the Content of [ButtonItem]
     * @see Content.contentColor
     * @return [T]
     */
    fun buttonContentColor( @ColorInt color: Int ) =
            thisRef.apply { buttonItem.contentColor( color ) }

    /**
     * Apply the [ColorRes] to the Content of [ButtonItem]
     * @see Content.contentColorRes
     * @return [T]
     */
    fun buttonContentColorRes( @ColorRes res: Int ) =
            thisRef.apply { buttonItem.contentColorRes( res ) }

    /**
     * Apply the [Spannable] to the Content of [ButtonItem]
     * @see Content.contentSpannable
     * @return [T]
     */
    fun buttonContentSpannable( spannable: Spannable ) =
            thisRef.apply { buttonItem.contentSpannable( spannable ) }

    /**
     * Apply the [StringRes] to the Content of [ButtonItem]
     * @see Content.contentStringRes
     * @return [T]
     */
    fun buttonContentStringRes( @StringRes res: Int ) =
            thisRef.apply { buttonItem.contentStringRes( res ) }

    /**
     * Apply the [CharSequence] to the Content of [ButtonItem]
     * @see Content.contentText
     * @return [T]
     */
    fun buttonContentText( text: CharSequence ) =
            thisRef.apply { buttonItem.contentText( text ) }

    /**
     * Apply the [StringRes] as styled text to the Content of [ButtonItem]
     * @see Content.contentTextRes
     * @return [T]
     */
    fun buttonContentTextRes ( @StringRes res: Int ) =
            thisRef.apply { buttonItem.contentTextRes( res ) }


    /**
     * Set the [ButtonStyle] to [buttonStyle]
     * @return [T]
     */
    fun buttonStyle( style: ButtonStyle ) = thisRef.apply { buttonStyle = style }
}

/** A typealias for [RippleBackgroundStyle] */
typealias ButtonStyle = RippleBackgroundStyle

/** A function for style a [PanelButton] within a DSL */
fun PanelButton<*>.button( f: ButtonItem.() -> Unit ) {
    buttonItem.f()
}

/** A [DslComponent] for call [PanelButton.buttonId] function */
var PanelButton<*>.buttonId: Int by dsl { buttonId( it ) }

/** A [DslComponent] for call [PanelButton.buttonBackgroundColor] function */
var PanelButton<*>.buttonBackgroundColor: Int by dsl { buttonBackgroundColor( it ) }

/** A [DslComponent] for call [PanelButton.buttonBackgroundColorRes] function */
var PanelButton<*>.buttonBackgroundColorRes: Int by dsl { buttonBackgroundColorRes( it ) }

/** A [DslComponent] for call [PanelButton.buttonBackgroundCornerRadiusDp] function */
var PanelButton<*>.buttonBackgroundCornerRadiusDp: Float by dsl { buttonBackgroundCornerRadiusDp( it ) }

/** A [DslComponent] for call [PanelButton.buttonBackgroundCornerRadiusPixel] function */
var PanelButton<*>.buttonBackgroundCornerRadiusPixel: Float by dsl { buttonBackgroundCornerRadiusPixel( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentDpSize] function */
var PanelButton<*>.buttonContentDpSize: Float by dsl { buttonContentDpSize( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentPixelSize] function */
var PanelButton<*>.buttonContentPixelSize: Float by dsl { buttonContentPixelSize( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentSpSize] function */
var PanelButton<*>.buttonContentSpSize: Float by dsl { buttonContentSpSize( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentBold] function */
var PanelButton<*>.buttonContentBold: Boolean by dsl { buttonContentBold( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentColor] function */
var PanelButton<*>.buttonContentColor: Int by dsl { buttonContentColor( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentColorRes] function */
var PanelButton<*>.buttonContentColorRes: Int by dsl { buttonContentColorRes( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentSpannable] function */
var PanelButton<*>.buttonContentSpannable: Spannable by dsl { buttonContentSpannable( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentStringRes] function */
var PanelButton<*>.buttonContentStringRes: Int by dsl { buttonContentStringRes( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentText] function */
var PanelButton<*>.buttonContentText: CharSequence by dsl { buttonContentText( it ) }

/** A [DslComponent] for call [PanelButton.buttonContentTextRes ] function */
var PanelButton<*>.buttonContentTextRes : Int by dsl { buttonContentTextRes ( it ) }