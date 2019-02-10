package studio.forface.materialbottombar.utils

import android.content.res.Resources

/**
 * Convert a dp value into the size in pixels.
 * @param dp the dp value
 * @return the size in pixels.
 */
internal fun dpToPixels( dp: Float ) = dp * Resources.getSystem().displayMetrics.density