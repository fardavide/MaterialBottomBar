package studio.forface.materialbottombar.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/*
 * Author: Davide Giuseppe Farella
 * A file containing utilities for Android
 */

/**
 * Call [Context.getColor] from API version 21-
 * @see ContextCompat.getColor
 */
fun Context.getColorCompat( @ColorRes res: Int ) =
        ContextCompat.getColor(this, res )