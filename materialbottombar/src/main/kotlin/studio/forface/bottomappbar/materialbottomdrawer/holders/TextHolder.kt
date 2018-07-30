package studio.forface.bottomappbar.materialbottomdrawer.holders

import android.text.Spannable
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible

class TextHolder internal constructor(
    @StringRes  val stringRes:  Int? =          null,
    @StringRes  val textRes:    Int? =          null,
    val text:       CharSequence? = null,
    val spannable:  Spannable? =    null
) {

    fun applyTo( textView: TextView ) {
        stringRes?.let {    textView.setText( it );     return }
        textRes?.let {      textView.setTextRes( it );  return }
        text?.let {         textView.text = it;         return }
        spannable?.let {    textView.text = it;         return }
    }

    fun applyToOrHide( textView: TextView ) {
        stringRes?.let {    textView.setText( it );     return }
        textRes?.let {      textView.setTextRes( it );  return }
        text?.let {         textView.text = it;         return }
        spannable?.let {    textView.text = it;         return }
        textView.isVisible = false
    }

    private fun TextView.setTextRes( @StringRes id: Int ) {
        text = context.getText( id )
    }
}