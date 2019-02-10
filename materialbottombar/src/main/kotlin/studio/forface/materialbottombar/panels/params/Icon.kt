@file:Suppress("unused")

package studio.forface.materialbottombar.panels.params

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import studio.forface.materialbottombar.dsl.DslComponent
import studio.forface.materialbottombar.dsl.dsl
import studio.forface.materialbottombar.panels.holders.ColorHolder
import studio.forface.materialbottombar.panels.holders.ImageHolder
import studio.forface.materialbottombar.panels.holders.ImageShape
import studio.forface.materialbottombar.panels.holders.SizeHolder
import java.io.File

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that have a customizable [Icon]
 * Inherit from [Param]
 */
interface Icon<T>: Param<T> {

    /** A reference to [ColorHolder] for of Color of the Icon */
    var iconColorHolder: ColorHolder
    /** A reference to [ImageHolder] for of Image of the Icon */
    var iconImageHolder: ImageHolder
    /** A reference to [SizeHolder] for of Size of the Icon */
    var iconSizeHolder: SizeHolder

    /**
     * Apply the [Icon] to the given [ImageView]
     * @param applyOrHide if true and no item is set for [iconImageHolder], the [ImageView] will be
     * hidden
     */
    fun applyIconTo( imageView: ImageView, applyOrHide: Boolean = false ) {
        if ( applyOrHide ) iconImageHolder.applyToOrHide( imageView )
        else iconImageHolder.applyTo( imageView )

        iconColorHolder.applyToImageView( imageView )
        iconSizeHolder.applyTo( imageView )
    }

    /**
     * Set the [iconColorHolder] with the given [ColorInt]
     * @return [T]
     */
    fun iconColor( @ColorInt color: Int ) =
            thisRef.apply { iconColorHolder = ColorHolder( color = color ) }

    /**
     * Set the [iconColorHolder] with the given [ColorRes]
     * @return [T]
     */
    fun iconColorRes( @ColorRes res: Int ) =
            thisRef.apply { iconColorHolder = ColorHolder( colorRes = res ) }


    /**
     * Set the [iconImageHolder] with the given [Bitmap]
     * @return [T]
     */
    fun iconBitmap( bitmap: Bitmap ) =
            thisRef.apply { iconImageHolder = ImageHolder( bitmap = bitmap ) }

    /**
     * Set the [iconImageHolder] with the given [Drawable]
     * @return [T]
     */
    fun iconDrawable( drawable: Drawable ) =
            thisRef.apply { iconImageHolder = ImageHolder( drawable = drawable ) }

    /**
     * Set the [iconImageHolder] with the given [File]
     * @return [T]
     */
    fun iconFile( file: File ) =
            thisRef.apply { iconImageHolder = ImageHolder( file = file ) }

    /**
     * Set the [iconImageHolder] with the given [DrawableRes]
     * @return [T]
     */
    fun iconResource( @DrawableRes res: Int ) =
            thisRef.apply { iconImageHolder = ImageHolder( res = res ) }

    /**
     * Set the [iconImageHolder] with the given [Uri]
     * @return [T]
     */
    fun iconUri( uri: Uri ) =
            thisRef.apply { iconImageHolder = ImageHolder( uri = uri ) }

    /**
     * Set the [iconImageHolder] with the given [String] url
     * @return [T]
     */
    fun iconUrl( url: String ) =
            thisRef.apply { iconImageHolder = ImageHolder( url = url ) }

    /**
     * Set the [iconImageHolder] with the given [ImageShape]
     * @return [T]
     */
    fun iconShape( imageShape: ImageShape ) =
            thisRef.apply{ iconImageHolder.imageShape = imageShape }


    /**
     * Set the [iconSizeHolder] with the given [Float] size in Dp
     */
    fun iconDpSize( size: Float ) =
            thisRef.apply { iconSizeHolder = SizeHolder( dp = size ) }

    /**
     * Set the [iconSizeHolder] with the given [Float] size in Pixel
     */
    fun iconPixelSize( size: Float ) =
            thisRef.apply { iconSizeHolder = SizeHolder( pixel = size ) }
}

/** A [DslComponent] for call [Icon.iconColor] function */
var Icon<*>.iconColor: Int by dsl { iconColor( it ) }

/** A [DslComponent] for call [Icon.iconColorRes] function */
var Icon<*>.iconColorRes: Int by dsl { iconColorRes( it ) }

/** A [DslComponent] for call [Icon.iconBitmap] function */
var Icon<*>.iconBitmap: Bitmap by dsl { iconBitmap( it ) }

/** A [DslComponent] for call [Icon.iconDrawable] function */
var Icon<*>.iconDrawable: Drawable by dsl { iconDrawable( it ) }

/** A [DslComponent] for call [Icon.iconFile] function */
var Icon<*>.iconFile: File by dsl { iconFile( it ) }

/** A [DslComponent] for call [Icon.iconResource] function */
var Icon<*>.iconResource: Int by dsl { iconResource( it ) }

/** A [DslComponent] for call [Icon.iconUri] function */
var Icon<*>.iconUri: Uri by dsl { iconUri( it ) }

/** A [DslComponent] for call [Icon.iconUrl] function */
var Icon<*>.iconUrl: String by dsl { iconUrl( it ) }

/** A [DslComponent] for call [Icon.iconShape] function */
var Icon<*>.iconShape: ImageShape by dsl { iconShape( it ) }

/** A [DslComponent] for call [Icon.iconDpSize] function */
var Icon<*>.iconDpSize: Float by dsl { iconDpSize( it ) }

/** A [DslComponent] for call [Icon.iconPixelSize] function */
var Icon<*>.iconPixelSize: Float by dsl { iconPixelSize( it ) }