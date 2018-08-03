package studio.forface.bottomappbar.utils

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat

fun TextView.setTextColorRes( @ColorRes res: Int ) {
    setTextColor( context.getColorCompat( res ) )
}

val View.constraintParams get() = layoutParams as? ConstraintLayout.LayoutParams

var View.elevationCompat: Float
    get() = ViewCompat.getElevation(this )
    set(value) = ViewCompat.setElevation(this, value )

fun View.setBackgroundColorRes( @ColorRes res: Int ) {
    setBackgroundColor( context.getColorCompat( res ) )
}