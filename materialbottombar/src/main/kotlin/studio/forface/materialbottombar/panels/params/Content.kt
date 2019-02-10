@file:Suppress("unused")

package studio.forface.materialbottombar.panels.params

import android.text.Spannable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import studio.forface.materialbottombar.dsl.DslComponent
import studio.forface.materialbottombar.dsl.dsl
import studio.forface.materialbottombar.panels.holders.ColorHolder
import studio.forface.materialbottombar.panels.holders.TextHolder
import studio.forface.materialbottombar.panels.holders.TextSizeHolder
import studio.forface.materialbottombar.panels.holders.TextStyleHolder

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that have a customizable [Content]
 * Inherit from [Param]
 */
interface Content<T>: Param<T> {

    /** A reference to [ColorHolder] for the Color of the Content */
    var contentColorHolder: ColorHolder
    /** A reference to [TextHolder] for the Text of the Content */
    var contentTextHolder: TextHolder
    /** A reference to [TextSizeHolder] for the Size of the Content */
    var contentTextSizeHolder: TextSizeHolder
    /** A reference to [TextStyleHolder] for the Style of the Content */
    var contentTextStyleHolder: TextStyleHolder

    /**
     * Apply the [Content] to the given [TextView]
     * @param applyOrHide if true and no item is set for [contentTextHolder], the [TextView] will be
     * hidden
     */
    fun applyContentTo( textView: TextView, applyOrHide: Boolean = false ) {
        if ( applyOrHide ) contentTextHolder.applyToOrHide( textView )
        else contentTextHolder.applyTo( textView )

        contentTextStyleHolder.   applyTo( textView )
        contentTextSizeHolder.    applyTo( textView )
        contentColorHolder.       applyToTextView( textView )
    }

    /**
     * Set the [contentColorHolder] with the given XXX
     * @return [T]
     */
    fun contentColor( @ColorInt color: Int ) =
            thisRef.apply { contentColorHolder = ColorHolder( color = color ) }

    /**
     * Set the [contentColorHolder] with the given XXX
     * @return [T]
     */
    fun contentColorRes( @ColorRes res: Int ) =
            thisRef.apply { contentColorHolder = ColorHolder( colorRes = res ) }


    /**
     * Set the [contentTextHolder] with the given [StringRes]
     * @return [T]
     */
    fun contentStringRes( @StringRes res: Int ) =
            thisRef.apply { contentTextHolder = TextHolder( stringRes = res) }

    /**
     * Set the [contentTextHolder] with the given [CharSequence]
     * @return [T]
     */
    fun contentText( text: CharSequence ) =
            thisRef.apply { contentTextHolder = TextHolder( text = text ) }

    /**
     * Set the [contentTextHolder] with the given [StringRes] as styleable text
     * @return [T]
     */
    fun contentTextRes( @StringRes res: Int ) =
            thisRef.apply { contentTextHolder = TextHolder( textRes = res) }

    /**
     * Set the [contentTextHolder] with the given [Spannable]
     * @return [T]
     */
    fun contentSpannable( spannable: Spannable) =
            thisRef.apply { contentTextHolder = TextHolder( spannable = spannable ) }


    /**
     * Set the [contentTextSizeHolder] with the given [Float] DP size
     * @return [T]
     */
    fun contentDpSize( size: Float ) =
            thisRef.apply { contentTextSizeHolder = TextSizeHolder( dp = size ) }

    /**
     * Set the [contentTextSizeHolder] with the given [Float] Pixel size
     * @return [T]
     */
    fun contentPixelSize( size: Float ) =
            thisRef.apply { contentTextSizeHolder = TextSizeHolder( pixel = size ) }

    /**
     * Set the [contentTextSizeHolder] with the given [Float] SP size
     * @return [T]
     */
    fun contentSpSize( size: Float ) =
            thisRef.apply { contentTextSizeHolder = TextSizeHolder( sp = size ) }


    /**
     * Set the [contentTextStyleHolder] with the given Bold style
     * @return [T]
     */
    fun contentBold( bold: Boolean = true ) =
            thisRef.apply { contentTextStyleHolder = TextStyleHolder( bold = bold ) }
}

/** A [DslComponent] for call [Content.contentColor] function */
var Content<*>.contentColor: Int by dsl { contentColor( it ) }

/** A [DslComponent] for call [Content.contentColorRes] function */
var Content<*>.contentColorRes: Int by dsl { contentColorRes( it ) }

/** A [DslComponent] for call [Content.contentStringRes] function */
var Content<*>.contentStringRes: Int by dsl { contentStringRes( it ) }

/** A [DslComponent] for call [Content.contentText] function */
var Content<*>.contentText: CharSequence by dsl { contentText( it ) }

/** A [DslComponent] for call [Content.contentTextRes] function */
var Content<*>.contentTextRes: Int by dsl { contentTextRes( it ) }

/** A [DslComponent] for call [Content.contentSpannable] function */
var Content<*>.contentSpannable: Spannable by dsl { contentSpannable( it ) }

/** A [DslComponent] for call [Content.contentDpSize] function */
var Content<*>.contentDpSize: Float by dsl { contentDpSize( it ) }

/** A [DslComponent] for call [Content.contentPixelSize] function */
var Content<*>.contentPixelSize: Float by dsl { contentPixelSize( it ) }

/** A [DslComponent] for call [Content.contentSpSize] function */
var Content<*>.contentSpSize: Float by dsl { contentSpSize( it ) }

/** A [DslComponent] for call [Content.contentBold] function */
var Content<*>.contentBold: Boolean by dsl { contentBold( it ) }