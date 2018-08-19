package studio.forface.bottomappbar.utils

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.children
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.show( show: Boolean ) {
    if      (   show && ! isOrWillBeShown  ) show()
    else if ( ! show && ! isOrWillBeHidden ) hide()
}

fun TextView.setTextColorRes( @ColorRes res: Int ) {
    setTextColor( context.getColorCompat( res ) )
}

val View.children get() = ( this as? ViewGroup )?.children ?: sequenceOf()

val View.constraintParams get() = layoutParams as? ConstraintLayout.LayoutParams

var View.elevationCompat: Float
    get() = ViewCompat.getElevation(this )
    set(value) = ViewCompat.setElevation(this, value )

fun View.setBackgroundColorRes( @ColorRes res: Int ) {
    setBackgroundColor( context.getColorCompat( res ) )
}