package studio.forface.materialbottombar.panels.holders

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.net.toFile
import studio.forface.theia.TheiaParams
import studio.forface.theia.TheiaParams.Shape
import studio.forface.theia.dsl.invoke
import studio.forface.theia.dsl.theia
import studio.forface.theia.toImageSource
import java.io.File

enum class ImageShape( internal val theiaShape: TheiaParams.Shape ) {
    ROUND  ( Shape.Round ),
    SQUARE ( Shape.Square )
}

class ImageHolder internal constructor(
    val bitmap:             Bitmap? =   null,
    val drawable:           Drawable? = null,
    val file:               File? =     null,
    @DrawableRes val res:   Int? =      null,
    val uri:                Uri? =      null,
    val url:                String? =   null
) {
    internal var imageShape = ImageShape.SQUARE

    fun applyTo( imageView: ImageView ) {
        imageView.theia {
            image = when {
                bitmap   != null -> bitmap.toImageSource()
                drawable != null -> drawable.toImageSource()
                file     != null -> file.toImageSource()
                res      != null -> res.toImageSource( imageView.resources )
                uri      != null -> uri.toFile().toImageSource()
                url      != null -> url.toImageSource()
                else -> return@theia
            }
            shape = imageShape.theiaShape
        }
    }

    fun applyToOrHide( imageView: ImageView ) {
        if ( allNull( bitmap, drawable, file, res, uri, url ) )
            imageView.visibility = View.GONE
        else applyTo( imageView )
    }

    private fun allNull( vararg any: Any? ) = any.all { it == null }
}