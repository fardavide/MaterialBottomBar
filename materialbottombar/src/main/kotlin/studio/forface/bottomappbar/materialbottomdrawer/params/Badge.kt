package studio.forface.bottomappbar.materialbottomdrawer.params

import android.text.Spannable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import studio.forface.bottomappbar.materialbottomdrawer.holders.ColorHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.TextHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.TextSizeHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.TextStyleHolder

enum class BadgeShape { SQUARE, ROUND }

internal interface Badge<T>: Param<T> {
    var badgeContentTextHolder: TextHolder
    var badgeContentTextStyleHolder: TextStyleHolder
    var badgeContentTextSizeHolder: TextSizeHolder
    var badgeContentColorHolder: ColorHolder

    var badgeBackgroundColorHolder: ColorHolder

    fun applyBadgeTo( textView: TextView ) {
        badgeContentTextHolder.         applyToOrHide( textView )
        badgeContentTextStyleHolder.    applyTo( textView )
        badgeContentTextSizeHolder.     applyTo( textView )
        badgeContentColorHolder.        applyToTextView( textView )

        badgeBackgroundColorHolder.     applyToBackground( textView )
    }

    fun withBadgeContentStringRes(@StringRes res: Int ) =
            thisRef.apply { badgeContentTextHolder = TextHolder( stringRes = res) }
    fun withBadgeContentTextRes(@StringRes res: Int ) =
            thisRef.apply { badgeContentTextHolder = TextHolder( textRes = res) }
    fun withBadgeContentText(text: CharSequence ) =
            thisRef.apply { badgeContentTextHolder = TextHolder( text = text ) }
    fun withBadgeContentSpannable(spannable: Spannable) =
            thisRef.apply { badgeContentTextHolder = TextHolder( spannable = spannable ) }

    fun withBadgeContentBold(bold: Boolean = true ) =
            thisRef.apply { badgeContentTextStyleHolder = TextStyleHolder( bold = bold ) }

    fun withBadgeContentPixelSize(size: Float ) =
            thisRef.apply { badgeContentTextSizeHolder = TextSizeHolder( pixel = size ) }
    fun withBadgeContentSpSize(size: Float ) =
            thisRef.apply { badgeContentTextSizeHolder = TextSizeHolder( sp = size ) }
    fun withBadgeContentDpSize(size: Float ) =
            thisRef.apply { badgeContentTextSizeHolder = TextSizeHolder( dp = size ) }

    fun withBadgeContentColorRes(@ColorRes res: Int ) =
            thisRef.apply { badgeContentColorHolder = ColorHolder( colorRes = res ) }
    fun withBadgeContentColor(@ColorInt color: Int ) =
            thisRef.apply { badgeContentColorHolder = ColorHolder( color = color ) }


    fun withBadgeBadgeBackgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { badgeBackgroundColorHolder = ColorHolder( colorRes = res ) }

    fun withBadgeBadgeBackgroundColor( @ColorInt color: Int ) =
            thisRef.apply { badgeBackgroundColorHolder = ColorHolder( color = color ) }
}