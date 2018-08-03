package studio.forface.bottomappbar.materialbottomdrawer.holders

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import java.io.File

enum class ImageShape( val requestOptions: RequestOptions ) {
    ROUND  ( RequestOptions.circleCropTransform() ),
    SQUARE ( RequestOptions.centerInsideTransform() )
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
        val glide = Glide.with( imageView )
        var requestBuilder: RequestBuilder<Drawable>? = null
        kotlin.run {
            bitmap?.let {   requestBuilder = glide.load( it ); return@run }
            drawable?.let { requestBuilder = glide.load( it ); return@run }
            file?.let {     requestBuilder = glide.load( it ); return@run }
            res?.let {      requestBuilder = glide.load( it ); return@run }
            uri?.let {      requestBuilder = glide.load( it ); return@run }
            url?.let {      requestBuilder = glide.load( it ); return@run }
        }
        requestBuilder?.apply( imageShape.requestOptions )?.into( imageView )
    }

    fun applyToOrHide( imageView: ImageView ) {
        if ( allNull( bitmap, drawable, file, res, uri, url ) )
            imageView.visibility = View.GONE
        else applyTo( imageView )
    }

    private fun allNull(vararg any: Any? ) = any.filterNotNull().isEmpty()
}