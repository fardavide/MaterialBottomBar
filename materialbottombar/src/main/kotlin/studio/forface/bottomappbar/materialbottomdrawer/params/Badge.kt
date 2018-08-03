package studio.forface.bottomappbar.materialbottomdrawer.params

import android.text.Spannable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.extra.BadgeItem

internal interface Badge<T>: Param<T> {
    var badgeItem: BadgeItem
    fun badgeItem( badge: BadgeItem ) =
            thisRef.apply { badgeItem = badge.copy() }

    fun applyBadgeTo( textView: TextView ) {
        badgeItem.applyTo( textView )
    }

    fun badgeContentStringRes( @StringRes res: Int ) =
            thisRef.apply { badgeItem.contentStringRes( res ) }
    fun badgeContentTextRes (@StringRes res: Int ) =
            thisRef.apply { badgeItem.contentTextRes( res ) }
    fun badgeContentText( text: CharSequence ) =
            thisRef.apply { badgeItem.contentText( text ) }
    fun badgeContentSpannable( spannable: Spannable ) =
            thisRef.apply { badgeItem.withContentSpannable( spannable ) }

    fun badgeContentBold( bold: Boolean = true ) =
            thisRef.apply { badgeItem.contentBold( bold ) }

    fun badgeContentPixelSize( size: Float ) =
            thisRef.apply { badgeItem.contentPixelSize( size ) }
    fun badgeContentSpSize( size: Float ) =
            thisRef.apply { badgeItem.contentSpSize( size ) }
    fun badgeContentDpSize( size: Float ) =
            thisRef.apply { badgeItem.contentDpSize( size ) }

    fun badgeContentColorRes( @ColorRes res: Int ) =
            thisRef.apply { badgeItem.contentColorRes( res ) }
    fun badgeContentColor( @ColorInt color: Int ) =
            thisRef.apply { badgeItem.contentColor( color ) }


    fun badgeBackgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { badgeItem.backgroundColorRes( res ) }
    fun badgeBackgroundColor( @ColorInt color: Int ) =
            thisRef.apply { badgeItem.backgroundColor( color ) }

    fun badgeBackgroundCornerRadiiPixel( pixel: Float ) =
            thisRef.apply { badgeItem.backgroundCornerRadiusPixel( pixel ) }
    fun badgeBackgroundCornerRadiusDp(dp: Float ) =
            thisRef.apply { badgeItem.backgroundCornerRadiusDp( dp ) }
}