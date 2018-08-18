package studio.forface.bottomappbar.materialpanels.params

import android.text.Spannable
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.extra.ButtonItem

typealias ButtonStyle = RippleBackgroundStyle

internal interface DrawerButton<T>: Param<T> {
    var buttonStyle: ButtonStyle
    var buttonItem: ButtonItem
    fun buttonItem( button: ButtonItem ) =
            thisRef.apply { buttonItem = button.copy() }

    fun applyButtonTo( button: Button) {
        buttonItem.applyTo( button, buttonStyle )
    }

    fun buttonContentStringRes( @StringRes res: Int ) =
            thisRef.apply { buttonItem.contentStringRes( res ) }
    fun buttonContentTextRes (@StringRes res: Int ) =
            thisRef.apply { buttonItem.contentTextRes( res ) }
    fun buttonContentText( text: CharSequence ) =
            thisRef.apply { buttonItem.contentText( text ) }
    fun buttonContentSpannable( spannable: Spannable ) =
            thisRef.apply { buttonItem.contentSpannable( spannable ) }

    fun buttonContentBold( bold: Boolean = true ) =
            thisRef.apply { buttonItem.contentBold( bold ) }

    fun buttonContentPixelSize( size: Float ) =
            thisRef.apply { buttonItem.contentPixelSize( size ) }
    fun buttonContentSpSize( size: Float ) =
            thisRef.apply { buttonItem.contentSpSize( size ) }
    fun buttonContentDpSize( size: Float ) =
            thisRef.apply { buttonItem.contentDpSize( size ) }

    fun buttonContentColorRes( @ColorRes res: Int ) =
            thisRef.apply { buttonItem.contentColorRes( res ) }
    fun buttonContentColor( @ColorInt color: Int ) =
            thisRef.apply { buttonItem.contentColor( color ) }


    fun buttonBackgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { buttonItem.backgroundColorRes( res ) }
    fun buttonBackgroundColor( @ColorInt color: Int ) =
            thisRef.apply { buttonItem.backgroundColor( color ) }

    fun buttonBackgroundCornerRadiusPixel( pixel: Float ) =
            thisRef.apply { buttonItem.backgroundCornerRadiusPixel( pixel ) }
    fun buttonBackgroundCornerRadiusDp( dp: Float ) =
            thisRef.apply { buttonItem.backgroundCornerRadiusDp( dp ) }

    fun buttonStyle( style: ButtonStyle ) =
            thisRef.apply { buttonStyle = style }
}