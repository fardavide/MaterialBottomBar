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

interface Content<T>: Param<T> {
    var contentTextHolder:        TextHolder
    var contentTextStyleHolder:   TextStyleHolder
    var contentTextSizeHolder:    TextSizeHolder
    var contentColorHolder:       ColorHolder

    fun applyContentTo( textView: TextView, applyOrHide: Boolean = false ) {
        if ( applyOrHide )
            contentTextHolder.applyToOrHide( textView )
        else
            contentTextHolder.applyTo( textView )
        contentTextStyleHolder.   applyTo( textView )
        contentTextSizeHolder.    applyTo( textView )
        contentColorHolder.       applyToTextView( textView )
    }

    fun contentStringRes(@StringRes res: Int ) =
            thisRef.apply { contentTextHolder = TextHolder( stringRes = res) }
    fun contentTextRes(@StringRes res: Int ) =
            thisRef.apply { contentTextHolder = TextHolder( textRes = res) }
    fun contentText(text: CharSequence ) =
            thisRef.apply { contentTextHolder = TextHolder( text = text ) }
    fun withContentSpannable( spannable: Spannable) =
            thisRef.apply { contentTextHolder = TextHolder( spannable = spannable ) }

    fun contentBold(bold: Boolean = true ) =
            thisRef.apply { contentTextStyleHolder = TextStyleHolder( bold = bold ) }

    fun contentPixelSize(size: Float ) =
            thisRef.apply { contentTextSizeHolder = TextSizeHolder( pixel = size ) }
    fun contentSpSize(size: Float ) =
            thisRef.apply { contentTextSizeHolder = TextSizeHolder( sp = size ) }
    fun contentDpSize(size: Float ) =
            thisRef.apply { contentTextSizeHolder = TextSizeHolder( dp = size ) }

    fun contentColorRes(@ColorRes res: Int ) =
            thisRef.apply { contentColorHolder = ColorHolder( colorRes = res ) }
    fun contentColor(@ColorInt color: Int ) =
            thisRef.apply { contentColorHolder = ColorHolder( color = color ) }
}