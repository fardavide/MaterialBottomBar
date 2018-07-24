package studio.forface.bottomappbar.utils

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat

fun TextView.setTextColorRes( @ColorRes res: Int ) {
    setTextColor( context.getColorCompat( res ) )
}

var View.elevationCompat: Float
    get() = ViewCompat.getElevation(this )
    set(value) = ViewCompat.setElevation(this, value )

fun View.setBackgroundColorRes( @ColorRes res: Int ) {
    setBackgroundColor( context.getColorCompat( res ) )
}