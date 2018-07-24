package studio.forface.bottomappbar.materialbottomdrawer.drawer

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import studio.forface.bottomappbar.materialbottomdrawer.holder.*
import java.io.File

class MaterialDrawer(
    val header: Header? = null
) {

    class Header (
        var icon:               Icon? =             null,
        var title:              Title? =            null,
        var backgroundColor:    BackgroundColor? =  null
    ) {

        class Icon {
            internal var imageHolder = ImageHolder()
            internal fun applyTo( imageView: ImageView ) { imageHolder.applyTo( imageView ) }

            fun withBitmap( bitmap: Bitmap ) =
                    apply { imageHolder = ImageHolder( bitmap = bitmap ) }
            fun withDrawable( drawable: Drawable ) =
                    apply { imageHolder = ImageHolder( drawable = drawable ) }
            fun withFIle( file: File ) =
                    apply { imageHolder = ImageHolder( file = file ) }
            fun withResource( @DrawableRes res: Int ) =
                    apply { imageHolder = ImageHolder( res = res ) }
            fun withUri( uri: Uri ) =
                    apply { imageHolder = ImageHolder( uri = uri ) }
            fun withUrl( url: String ) =
                    apply { imageHolder = ImageHolder( url = url ) }

            fun withShape( imageShape: ImageShape ) {
                imageHolder.imageShape = imageShape
            }
        }

        class Title {
            internal var textHolder = TextHolder()
            internal var textStyleHolder = TextStyleHolder()
            internal var colorHolder = ColorHolder()
            internal fun applyTo( textView: TextView ) {
                textHolder.applyTo( textView )
                textStyleHolder.applyTo( textView )
                colorHolder.applyToText( textView )
            }

            fun withStringRes( @StringRes res: Int ) =
                    apply { textHolder = TextHolder( stringRes = res) }
            fun withTextRes( @StringRes res: Int ) =
                    apply { textHolder = TextHolder( textRes = res) }
            fun withText( text: CharSequence ) =
                    apply { textHolder = TextHolder( text = text ) }
            fun withSpannable( spannable: Spannable ) =
                    apply { textHolder = TextHolder( spannable = spannable ) }

            fun withBold( bold: Boolean ) =
                    apply { textStyleHolder = TextStyleHolder( bold = bold ) }

            fun withColorRes( @ColorRes res: Int ) =
                    apply { colorHolder = ColorHolder( colorRes = res ) }
            fun withColor( @ColorInt color: Int ) =
                    apply { colorHolder = ColorHolder( color = color ) }
        }

        class BackgroundColor {
            internal var colorHolder = ColorHolder()
            internal fun applyTo( view: View ) {
                colorHolder.applyToBackground( view )
            }

            fun withColorRes( @ColorRes res: Int ) =
                    apply { colorHolder = ColorHolder( colorRes = res ) }
            fun withColor( @ColorInt color: Int ) =
                    apply { colorHolder = ColorHolder( color = color ) }
        }

    }

    class Body

}