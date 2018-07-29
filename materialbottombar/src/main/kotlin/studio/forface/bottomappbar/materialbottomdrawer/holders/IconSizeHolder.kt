package studio.forface.bottomappbar.materialbottomdrawer.holders

import android.widget.ImageView
import studio.forface.bottomappbar.utils.dpToPixels

class IconSizeHolder internal constructor(
    val pixel:  Float? = null,
    val dp:     Float? = null
) {

    fun applyTo( imageView: ImageView) {
        val size = when {
            pixel   != null -> pixel.toInt()
            dp      != null -> dpToPixels( dp ).toInt()
            else -> null
        }

        size?.let { imageView.layoutParams.apply {
            height = size
            width = size
        } }
    }
}