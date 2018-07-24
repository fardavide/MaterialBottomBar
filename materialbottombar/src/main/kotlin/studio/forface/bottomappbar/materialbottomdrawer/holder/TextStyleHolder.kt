package studio.forface.bottomappbar.materialbottomdrawer.holder

import android.graphics.Typeface
import android.widget.TextView

class TextStyleHolder internal constructor(
    val bold: Boolean? = null
) {

    fun applyTo( textView: TextView ) {
        bold?.let {
            textView.typeface = if ( bold ) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
    }

}