package studio.forface.bottomappbar.panels.holders

import android.view.View
import studio.forface.bottomappbar.utils.dpToPixels

class ViewSizeHolder internal constructor(
    val sizesPixel:     Pair<Float?, Float?>? = null,
    val sizesDp:        Pair<Float?, Float?>? = null
) {

    fun applyTo( view: View ) {
        var ( width, height ) = when {
            sizesPixel  != null ->
                sizesPixel.first?.toInt() to sizesPixel.second?.toInt()
            sizesDp     != null -> {
                val w = sizesDp.first?.let { dpToPixels( it ).toInt() }
                val h = sizesDp.second?.let { dpToPixels( it ).toInt() }
                w to h
            }
            else -> null to null
        }

        if ( width  == null && height != null ) width = height
        if ( height == null && width  != null ) height = width

        if ( width != null && height != null ) {
            view.layoutParams.apply {
                this.width = width
                this.height = height
            }
        }
    }
}