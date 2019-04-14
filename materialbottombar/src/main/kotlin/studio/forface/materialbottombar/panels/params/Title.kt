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
 * An interface for items that have a customizable Title.
 * Inherit from [Param]
 */
interface Title<T>: Param<T> {

    /** A reference to [ColorHolder] for the Color of the Title */
    var titleColorHolder: ColorHolder
    /** A reference to [TextHolder] for the Text of the Title */
    var titleTextHolder: TextHolder
    /** A reference to [TextSizeHolder] for the Size of the Title */
    var titleTextSizeHolder: TextSizeHolder
    /** A reference to [TextStyleHolder] for the Style of the Title */
    var titleTextStyleHolder: TextStyleHolder

    /**
     * Apply the [Title] to the given [TextView]
     * @param defaultColor an OPTIONAL [ColorInt] to use al fallback
     */
    fun applyTitleTo( textView: TextView, @ColorInt defaultColor: Int? = null ) {
        titleTextHolder.        applyTo( textView )
        titleTextStyleHolder.   applyTo( textView )
        titleTextSizeHolder.    applyTo( textView )
        titleColorHolder.       applyToTextView( textView, defaultColor )
    }

    /**
     * Set the [titleColorHolder] with the given [ColorInt]
     * @return [T]
     */
    fun titleColor( @ColorInt color: Int ) =
            thisRef.apply { titleColorHolder = ColorHolder( color = color ) }

    /**
     * Set the [titleColorHolder] with the given [ColorRes]
     * @return [T]
     */
    fun titleColorRes( @ColorRes res: Int ) =
            thisRef.apply { titleColorHolder = ColorHolder( colorRes = res ) }


    /**
     * Set the [titleTextHolder] with the given [Spannable]
     * @return [T]
     */
    fun titleSpannable( spannable: Spannable ) =
            thisRef.apply { titleTextHolder = TextHolder( spannable = spannable ) }

    /**
     * Set the [titleTextHolder] with the given [StringRes]
     * @return [T]
     */
    fun titleStringRes( @StringRes res: Int ) =
            thisRef.apply { titleTextHolder = TextHolder( stringRes = res) }

    /**
     * Set the [titleTextHolder] with the given [CharSequence]
     * @return [T]
     */
    fun titleText( text: CharSequence ) =
            thisRef.apply { titleTextHolder = TextHolder( text = text ) }

    /**
     * Set the [titleTextHolder] with the given [StringRes] ( styled text )
     * @return [T]
     */
    fun titleTextRes( @StringRes res: Int ) =
            thisRef.apply { titleTextHolder = TextHolder( textRes = res) }


    /**
     * Set the [titleTextSizeHolder] with the given [Float] size in DP
     * @return [T]
     */
    fun titleDpSize( size: Float ) =
            thisRef.apply { titleTextSizeHolder = TextSizeHolder( dp = size ) }

    /**
     * Set the [titleTextSizeHolder] with the given [Float] size in Pixel
     * @return [T]
     */
    fun titlePixelSize( size: Float ) =
            thisRef.apply { titleTextSizeHolder = TextSizeHolder( pixel = size ) }

    /**
     * Set the [titleTextSizeHolder] with the given [Float] size in SP
     * @return [T]
     */
    fun titleSpSize( size: Float ) =
            thisRef.apply { titleTextSizeHolder = TextSizeHolder( sp = size ) }


    /**
     * Set the [titleTextStyleHolder] with the given [Boolean] bold value
     * @return [T]
     */
    fun titleBold( bold: Boolean = true ) =
            thisRef.apply { titleTextStyleHolder = TextStyleHolder( bold = bold ) }
}

/** A [DslComponent] for call [Title.titleColor] function */
var Title<*>.titleColor: Int by dsl { titleColor( it ) }

/** A [DslComponent] for call [Title.titleColorRes] function */
var Title<*>.titleColorRes: Int by dsl { titleColorRes( it ) }

/** A [DslComponent] for call [Title.titleSpannable] function */
var Title<*>.titleSpannable: Spannable by dsl { titleSpannable( it ) }

/** A [DslComponent] for call [Title.titleStringRes] function */
var Title<*>.titleStringRes: Int by dsl { titleStringRes( it ) }

/** A [DslComponent] for call [Title.titleText] function */
var Title<*>.titleText: CharSequence by dsl { titleText( it ) }

/** A [DslComponent] for call [Title.titleTextRes] function */
var Title<*>.titleTextRes: Int by dsl { titleTextRes( it ) }

/** A [DslComponent] for call [Title.titleDpSize] function */
var Title<*>.titleDpSize: Float by dsl { titleDpSize( it ) }

/** A [DslComponent] for call [Title.titlePixelSize] function */
var Title<*>.titlePixelSize: Float by dsl { titlePixelSize( it ) }

/** A [DslComponent] for call [Title.titleSpSize] function */
var Title<*>.titleSpSize: Float by dsl { titleSpSize( it ) }

/** A [DslComponent] for call [Title.titleBold] function */
var Title<*>.titleBold: Boolean by dsl { titleBold( it ) }