package studio.forface.bottomappbar.materialbottomdrawer.params

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import studio.forface.bottomappbar.materialbottomdrawer.holders.ColorHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.SizeHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.ImageHolder
import studio.forface.bottomappbar.materialbottomdrawer.holders.ImageShape
import java.io.File

interface Icon<T>: Param<T> {
    var iconImageHolder: ImageHolder
    var iconColorHolder: ColorHolder
    var iconSizeHolder: SizeHolder

    fun applyIconTo( imageView: ImageView, applyOrHide: Boolean = false ) {
        if ( applyOrHide )
            iconImageHolder.applyToOrHide( imageView )
        else
            iconImageHolder.applyTo( imageView )
        iconColorHolder.applyToImageView( imageView )
        iconSizeHolder.applyTo( imageView )
    }

    fun withIconBitmap( bitmap: Bitmap) =
            thisRef.apply { iconImageHolder = ImageHolder( bitmap = bitmap ) }
    fun withIconDrawable( drawable: Drawable) =
            thisRef.apply { iconImageHolder = ImageHolder( drawable = drawable ) }
    fun withIconFile( file: File) =
            thisRef.apply { iconImageHolder = ImageHolder( file = file ) }
    fun iconResource(@DrawableRes res: Int ) =
            thisRef.apply { iconImageHolder = ImageHolder( res = res ) }
    fun withIconUri( uri: Uri) =
            thisRef.apply { iconImageHolder = ImageHolder( uri = uri ) }
    fun iconUrl(url: String ) =
            thisRef.apply { iconImageHolder = ImageHolder( url = url ) }

    fun withIconColorRes( @ColorRes res: Int ) =
            thisRef.apply { iconColorHolder = ColorHolder( colorRes = res ) }
    fun iconColor(color: Int ) =
            thisRef.apply { iconColorHolder = ColorHolder( color = color ) }

    fun withIconPixelSize( size: Float ) =
            thisRef.apply { iconSizeHolder = SizeHolder( pixel = size ) }
    fun withIconDpSize( size: Float ) =
            thisRef.apply { iconSizeHolder = SizeHolder( dp = size ) }

    fun withIconShape( imageShape: ImageShape) {
        iconImageHolder.imageShape = imageShape
    }
}