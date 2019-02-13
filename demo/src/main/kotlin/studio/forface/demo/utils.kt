package studio.forface.demo

import android.content.res.Resources

/*
 * Author: Davide Giuseppe Farella.
 */

/** A [Float] representing the density of the display, for convert Dp's and Pixel's */
private val displayDensity = Resources.getSystem().displayMetrics.density

/** @return a number of pixel converted from the given Dp [Int] */
val Int.dp: Int get() = ( this * displayDensity ).toInt()