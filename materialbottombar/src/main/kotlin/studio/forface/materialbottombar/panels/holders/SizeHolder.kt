package studio.forface.materialbottombar.panels.holders

import android.widget.ImageView
import studio.forface.materialbottombar.utils.dpToPixels

class SizeHolder internal constructor(
    val pixel:  Float? = null,
    val dp:     Float? = null
) {

    fun applyTo( imageView: ImageView) {
        resolveIntSize()?.let { imageView.layoutParams.apply {
            height = it
            width =  it
        } }
    }

    fun resolveFloatSize() = when {
        pixel   != null -> pixel
        dp      != null -> dpToPixels( dp )
        else -> null
    }

    fun resolveIntSize() = when {
        pixel   != null -> pixel.toInt()
        dp      != null -> dpToPixels( dp ).toInt()
        else -> null
    }

}