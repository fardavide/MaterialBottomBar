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

interface Title<T>: Param<T> {
    var titleTextHolder:        TextHolder
    var titleTextStyleHolder:   TextStyleHolder
    var titleTextSizeHolder:    TextSizeHolder
    var titleColorHolder:       ColorHolder

    fun applyTitleTo( textView: TextView) {
        titleTextHolder.        applyTo( textView )
        titleTextStyleHolder.   applyTo( textView )
        titleTextSizeHolder.    applyTo( textView )
        titleColorHolder.       applyToTextView( textView )
    }

    fun withTitleStringRes( @StringRes res: Int ) =
            thisRef.apply { titleTextHolder = TextHolder( stringRes = res) }
    fun withTitleTextRes( @StringRes res: Int ) =
            thisRef.apply { titleTextHolder = TextHolder( textRes = res) }
    fun withTitleText( text: CharSequence ) =
            thisRef.apply { titleTextHolder = TextHolder( text = text ) }
    fun withTitleSpannable( spannable: Spannable) =
            thisRef.apply { titleTextHolder = TextHolder( spannable = spannable ) }

    fun withTitleBold( bold: Boolean = true ) =
            thisRef.apply { titleTextStyleHolder = TextStyleHolder( bold = bold ) }

    fun withTitlePixelSize( size: Float ) =
            thisRef.apply { titleTextSizeHolder = TextSizeHolder( pixel = size ) }
    fun withTitleSpSize( size: Float ) =
            thisRef.apply { titleTextSizeHolder = TextSizeHolder( sp = size ) }
    fun withTitleDpSize( size: Float ) =
            thisRef.apply { titleTextSizeHolder = TextSizeHolder( dp = size ) }

    fun withTitleColorRes( @ColorRes res: Int ) =
            thisRef.apply { titleColorHolder = ColorHolder( colorRes = res ) }
    fun withTitleColor( @ColorInt color: Int ) =
            thisRef.apply { titleColorHolder = ColorHolder( color = color ) }
}