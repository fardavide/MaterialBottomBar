package studio.forface.bottomappbar.materialpanels.holders

import android.util.TypedValue
import android.widget.TextView

class TextSizeHolder internal constructor(
    val pixel:  Float? = null,
    val sp:     Float? = null,
    val dp:     Float? = null
) {

    fun applyTo( textView: TextView ) {
        val ( unit, size ) = when {
            pixel   != null -> TypedValue.COMPLEX_UNIT_PX to pixel
            sp      != null -> TypedValue.COMPLEX_UNIT_SP to sp
            dp      != null -> TypedValue.COMPLEX_UNIT_DIP to dp
            else -> null to null
        }

        unit?.let { textView.setTextSize( unit, size!! ) }
    }
}