@file:Suppress("unused")

package studio.forface.bottomappbar.panels.params

import android.text.Spannable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import studio.forface.bottomappbar.dsl.DslComponent
import studio.forface.bottomappbar.dsl.dsl
import studio.forface.bottomappbar.panels.items.extra.BadgeItem

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that have a customizable [Badge]
 *
 * Inherit from [Param]
 */
interface Badge<T>: Param<T> {

    /** A reference to [BadgeItem] for the Badge */
    var badgeItem: BadgeItem

    /** Apply the [Badge] to the given [TextView] */
    fun applyBadgeTo( textView: TextView ) {
        badgeItem.applyTo( textView )
    }

    /**
     * Set a new [BadgeItem]
     * @return [T]
     */
    fun badgeItem( badge: BadgeItem ) = thisRef.apply { badgeItem = badge.cloneRef() }

    /**
     * Apply the [ColorInt] to the Background of [badgeItem]
     * @see Background.backgroundColor
     * @return [T]
     */
    fun badgeBackgroundColor( @ColorInt color: Int ) =
            thisRef.apply { badgeItem.backgroundColor( color ) }

    /**
     * Apply the [ColorRes] to the Background of [badgeItem]
     * @see [Background.backgroundColorRes]
     * @return [T]
     */
    fun badgeBackgroundColorRes( @ColorRes res: Int ) =
            thisRef.apply { badgeItem.backgroundColorRes( res ) }

    /**
     * Apply the corner radius in DP size to the Background of [badgeItem]
     * @see Background.backgroundCornerRadiusDp
     * @return [T]
     */
    fun badgeBackgroundCornerRadiusDp( dp: Float ) =
            thisRef.apply { badgeItem.backgroundCornerRadiusDp( dp ) }

    /**
     * Apply the corner radius in Pixel size to the Background of [badgeItem]
     * @see Background.backgroundCornerRadiusPixel
     * @return [T]
     */
    fun badgeBackgroundCornerRadiusPixel( pixel: Float ) =
            thisRef.apply { badgeItem.backgroundCornerRadiusPixel( pixel ) }

    /**
     * Apply the size in DP to the Content of the [badgeItem]
     * @see Content.contentDpSize
     * @return [T]
     */
    fun badgeContentDpSize( size: Float ) =
            thisRef.apply { badgeItem.contentDpSize( size ) }

    /**
     * Apply the size in Pixel to the Content of the [badgeItem]
     * @see Content.contentPixelSize
     * @return [T]
     */
    fun badgeContentPixelSize( size: Float ) =
            thisRef.apply { badgeItem.contentPixelSize( size ) }

    /**
     * Apply the size in SP to the Content of the [badgeItem]
     * @see Content.contentSpSize
     * @return [T]
     */
    fun badgeContentSpSize( size: Float ) =
            thisRef.apply { badgeItem.contentSpSize( size ) }

    /**
     * Apply the bold to the Style of Content of the [badgeItem]
     * @see Content.contentBold
     * @return [T]
     */
    fun badgeContentBold( bold: Boolean = true ) =
            thisRef.apply { badgeItem.contentBold( bold ) }

    /**
     * Apply the [ColorInt] to the Content of [badgeItem]
     * @see Content.contentColor
     * @return [T]
     */
    fun badgeContentColor( @ColorInt color: Int ) =
            thisRef.apply { badgeItem.contentColor( color ) }

    /**
     * Apply the [ColorRes] to the Content of [badgeItem]
     * @see Content.contentColorRes
     * @return [T]
     */
    fun badgeContentColorRes( @ColorRes res: Int ) =
            thisRef.apply { badgeItem.contentColorRes( res ) }

    /**
     * Apply the [Spannable] to the Content of [badgeItem]
     * @see Content.contentSpannable
     * @return [T]
     */
    fun badgeContentSpannable( spannable: Spannable ) =
            thisRef.apply { badgeItem.contentSpannable( spannable ) }

    /**
     * Apply the [StringRes] to the Content of [badgeItem]
     * @see Content.contentStringRes
     * @return [T]
     */
    fun badgeContentStringRes( @StringRes res: Int ) =
            thisRef.apply { badgeItem.contentStringRes( res ) }

    /**
     * Apply the [CharSequence] to the Content of [badgeItem]
     * @see Content.contentText
     * @return [T]
     */
    fun badgeContentText( text: CharSequence ) =
            thisRef.apply { badgeItem.contentText( text ) }

    /**
     * Apply the [StringRes] as styled text to the Content of [badgeItem]
     * @see Content.contentTextSizeHolder
     * @return [T]
     */
    fun badgeContentTextRes ( @StringRes res: Int ) =
            thisRef.apply { badgeItem.contentTextRes( res ) }
}

/** A function for style a [Badge] within a DSL */
fun Badge<*>.badge( f: BadgeItem.() -> Unit ) {
    badgeItem.f()
}

/** A [DslComponent] for call [Badge.badgeBackgroundColor] */
var Badge<*>.badgeBackgroundColor: Int by dsl { badgeBackgroundColor( it ) }

/** A [DslComponent] for call [Badge.badgeBackgroundColorRes] */
var Badge<*>.badgeBackgroundColorRes: Int by dsl { badgeBackgroundColorRes( it ) }

/** A [DslComponent] for call [Badge.badgeBackgroundCornerRadiusDp] */
var Badge<*>.badgeBackgroundCornerRadiusDp: Float by dsl { badgeBackgroundCornerRadiusDp( it ) }

/** A [DslComponent] for call [Badge.badgeBackgroundCornerRadiusPixel] */
var Badge<*>.badgeBackgroundCornerRadiusPixel: Float by dsl { badgeBackgroundCornerRadiusPixel( it ) }

/** A [DslComponent] for call [Badge.badgeContentDpSize] */
var Badge<*>.badgeContentDpSize: Float by dsl { badgeContentDpSize( it ) }

/** A [DslComponent] for call [Badge.badgeContentPixelSize] */
var Badge<*>.badgeContentPixelSize: Float by dsl { badgeContentPixelSize( it ) }

/** A [DslComponent] for call [Badge.badgeContentSpSize] */
var Badge<*>.badgeContentSpSize: Float by dsl { badgeContentSpSize( it ) }

/** A [DslComponent] for call [Badge.badgeContentBold] */
var Badge<*>.badgeContentBold: Boolean by dsl { badgeContentBold( it ) }

/** A [DslComponent] for call [Badge.badgeContentColor] */
var Badge<*>.badgeContentColor: Int by dsl { badgeContentColor( it ) }

/** A [DslComponent] for call [Badge.badgeContentColorRes] */
var Badge<*>.badgeContentColorRes: Int by dsl { badgeContentColorRes( it ) }

/** A [DslComponent] for call [Badge.badgeContentStringRes] */
var Badge<*>.badgeContentStringRes: Int by dsl { badgeContentStringRes( it ) }

/** A [DslComponent] for call [Badge.badgeContentText] */
var Badge<*>.badgeContentText: CharSequence by dsl { badgeContentText( it ) }

/** A [DslComponent] for call [Badge.badgeContentTextRes ] */
var Badge<*>.badgeContentTextRes : Int by dsl { badgeContentTextRes ( it ) }

/** A [DslComponent] for call [Badge.badgeContentSpannable] */
var Badge<*>.badgeContentSpannable: Spannable by dsl { badgeContentSpannable( it ) }
