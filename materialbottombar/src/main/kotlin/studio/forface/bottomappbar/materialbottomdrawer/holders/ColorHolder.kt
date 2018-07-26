@file:Suppress("MemberVisibilityCanBePrivate")

package studio.forface.bottomappbar.materialbottomdrawer.holders

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.graphics.drawable.DrawableCompat
import studio.forface.bottomappbar.utils.getColorCompat
import studio.forface.bottomappbar.utils.setTextColorRes

class ColorHolder internal constructor(
    @ColorRes   val colorRes:   Int? =          null,
    @ColorInt   val color:      Int? =          null
) {

    fun applyToBackground( view: View ) {
        val color = resolveColor( view.context )
        try {
            DrawableCompat.setTint( view.background, color )
        } catch ( e: NullPointerException ) {
            view.setBackgroundColor( color )
        }
    }

    fun applyToDrawable( context: Context, drawable: Drawable ) {
        val color = resolveColor( context )
        DrawableCompat.setTint( drawable, color )
    }

    fun applyToDrawable( button: ImageButton ) {
        val color = resolveColor( button.context )
        DrawableCompat.setTint( button.drawable, color )
    }

    fun applyToImageView( imageView: ImageView  ) {
        colorRes?.let { imageView.setColorFilter( imageView.context.getColorCompat( it ) ); return }
        color?.let {    imageView.setColorFilter( it );                                     return }
    }

    fun applyToTextView( textView: TextView  ) {
        colorRes?.let { textView.setTextColorRes( it );   return }
        color?.let {    textView.setTextColor( it );      return }
    }

    fun resolveColor( context: Context ) = when {
        colorRes != null -> context.getColorCompat( colorRes )
        color != null -> color
        else -> throw IllegalStateException( "ColorHolder has no value" )
    }

}