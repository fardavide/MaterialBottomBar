package studio.forface.materialbottombar.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Build
import com.google.android.material.shape.*

/**
 * @author Davide Giuseppe Farella
 * On object for create [Drawable]
 */
object Drawables {

    /** A [Float] representing the default corner radius */
    internal const val CORNER_RADIUS_SOFT = 12f

    /**
     * Create a [Drawable] with ripple effect when touched
     * @see RippleDrawable for [Build.VERSION_CODES.LOLLIPOP]
     * @see StateListDrawable for lower Android versions
     */
    fun selectableDrawable(
            color: Int,
            cornerRadius: Float,
            startTransparent: Boolean = true
    ): Drawable {
        val contentColor =  if ( startTransparent ) Color.TRANSPARENT else color
        val contentAlpha =  if ( startTransparent ) 0f else 1f
        val maskColor =     if ( startTransparent ) color else Color.BLACK
        val maskAlpha =     if ( startTransparent ) 1f else 0.5f

        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ) {
            val stateListDrawable = StateListDrawable()
            stateListDrawable.addState(
                    intArrayOf( android.R.attr.state_pressed ),
                    ColorDrawable( color )
            )
            stateListDrawable.addState(
                    intArrayOf( android.R.attr.state_focused ),
                    ColorDrawable( color )
            )
            stateListDrawable.addState(
                    intArrayOf(),
                    ColorDrawable( contentColor )
            )
            return stateListDrawable

        } else {
            val colorList = ColorStateList.valueOf( maskColor )
            val content = materialDrawable( contentColor, cornerRadius, contentAlpha )
            val mask = materialDrawable( maskColor, cornerRadius, maskAlpha )
            return RippleDrawable( colorList, content, mask )
        }
    }

    /** Create a [MaterialShapeDrawable] */
    fun materialDrawable( color: Int, cornerRadius: Float, colorAlpha: Float = 1f ): Drawable {
        val shapedPathModel = ShapeAppearanceModel().apply {
            setAllEdges( EdgeTreatment() )
            setAllCorners( RoundedCornerTreatment( cornerRadius ) )
        }

        return MaterialShapeDrawable( shapedPathModel ).apply {
            setTint( color )
            alpha = ( colorAlpha * 255 ).toInt()
        }
    }
}