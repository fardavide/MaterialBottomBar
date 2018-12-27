package studio.forface.bottomappbar.utils

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.children
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
 * Author: Davide Giuseppe Farella.
 * A file containing utilities for Android's View
 */

/** Call [FloatingActionButton.show] if [show] is true, else [FloatingActionButton.hide] */
internal fun FloatingActionButton.show( show: Boolean ) {
    if      (   show && ! isOrWillBeShown  ) show()
    else if ( ! show && ! isOrWillBeHidden ) hide()
}

/** Call [TextView.setTextColor] with a [ColorRes] */
internal fun TextView.setTextColorRes( @ColorRes res: Int ) {
    setTextColor( context.getColorCompat( res ) )
}

/**
 * Get the [View] children of a [View]
 * If [View] is not a [ViewGroup], return an empty [Sequence]
 * @return [Sequence] of [View]
 */
internal val View.children get() = ( this as? ViewGroup )?.children ?: sequenceOf()

/**
 * Get [View.getLayoutParams] casted as [ConstraintLayout.LayoutParams] if the casting is possible,
 * else return null.
 * @return OPTIONAL [ConstraintLayout.LayoutParams]
 */
internal val View.constraintParams get() = layoutParams as? ConstraintLayout.LayoutParams

/** Get [View.getElevation] and Set [View.setElevation] via [ViewCompat] */
internal var View.elevationCompat: Float
    get() = ViewCompat.getElevation(this )
    set(value) = ViewCompat.setElevation(this, value )

/**
 * Find a child of the [ViewGroup] that matches the queried type [T]
 * @return OPTIONAL [T]
 */
internal inline fun <reified T> ViewGroup.findChildType() = children.find { it is T } as? T

/**
 * Filter the children of the [ViewGroup] that match the queried type [T]
 * @return [Sequence] of [T]
 */
@Suppress("UNCHECKED_CAST")
internal inline fun <reified T> ViewGroup.filterChildType() = children.filter { it is T } as Sequence<T>

/** Call [View.setBackground] with a [ColorRes] */
internal fun View.setBackgroundColorRes( @ColorRes res: Int ) {
    setBackgroundColor( context.getColorCompat( res ) )
}