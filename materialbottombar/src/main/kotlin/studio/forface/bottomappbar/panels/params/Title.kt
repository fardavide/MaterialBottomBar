package studio.forface.bottomappbar.panels.params

import android.text.Spannable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import studio.forface.bottomappbar.panels.holders.ColorHolder
import studio.forface.bottomappbar.panels.holders.TextHolder
import studio.forface.bottomappbar.panels.holders.TextSizeHolder
import studio.forface.bottomappbar.panels.holders.TextStyleHolder

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

    fun titleStringRes( @StringRes res: Int ) =
            thisRef.apply { titleTextHolder = TextHolder( stringRes = res) }
    fun titleTextRes( @StringRes res: Int ) =
            thisRef.apply { titleTextHolder = TextHolder( textRes = res) }
    fun titleText( text: CharSequence ) =
            thisRef.apply { titleTextHolder = TextHolder( text = text ) }
    fun titleSpannable( spannable: Spannable) =
            thisRef.apply { titleTextHolder = TextHolder( spannable = spannable ) }

    fun titleBold( bold: Boolean = true ) =
            thisRef.apply { titleTextStyleHolder = TextStyleHolder( bold = bold ) }

    fun titlePixelSize( size: Float ) =
            thisRef.apply { titleTextSizeHolder = TextSizeHolder( pixel = size ) }
    fun titleSpSize( size: Float ) =
            thisRef.apply { titleTextSizeHolder = TextSizeHolder( sp = size ) }
    fun titleDpSize( size: Float ) =
            thisRef.apply { titleTextSizeHolder = TextSizeHolder( dp = size ) }

    fun titleColorRes( @ColorRes res: Int ) =
            thisRef.apply { titleColorHolder = ColorHolder( colorRes = res ) }
    fun titleColor( @ColorInt color: Int ) =
            thisRef.apply { titleColorHolder = ColorHolder( color = color ) }
}