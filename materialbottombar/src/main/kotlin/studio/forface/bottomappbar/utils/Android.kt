package studio.forface.bottomappbar.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat( @ColorRes res: Int ) =
        ContextCompat.getColor(this, res )