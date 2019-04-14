package studio.forface.materialbottombar.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel

/**
 * @author Davide Giuseppe Farella
 * On object for create [Drawable]
 */
object Drawables {

    /** A [Float] representing the default corner radius */
    internal const val CORNER_RADIUS_SOFT = 12f

    /**
     * @return a [Drawable] with ripple effect when touched
     * @see RippleDrawable for [Build.VERSION_CODES.LOLLIPOP]
     * @see StateListDrawable for lower Android versions
     */
    fun selectableDrawable(
            color: Int,
            cornerRadius: Float,
            startTransparent: Boolean = true
    ) = selectableDrawable( color, color, cornerRadius, startTransparent )

    /**
     * @return a [Drawable] with ripple effect when touched
     * @see RippleDrawable for [Build.VERSION_CODES.LOLLIPOP]
     * @see StateListDrawable for lower Android versions
     */
    fun selectableDrawable(
            color: Int,
            rippleColor: Int,
            cornerRadius: Float,
            startTransparent: Boolean = true
    ): Drawable {
        val contentColor =  if ( startTransparent ) Color.TRANSPARENT else color
        val contentAlpha =  if ( startTransparent ) 0f else 1f
        val maskColor =     if ( startTransparent ) rippleColor else Color.BLACK
        val maskAlpha =     if ( startTransparent ) 1f else 0.5f

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            val colorList = ColorStateList.valueOf( maskColor )
            val content = materialDrawable( contentColor, cornerRadius, contentAlpha )
            val mask = materialDrawable( maskColor, cornerRadius, maskAlpha )
            return RippleDrawable( colorList, content, mask )

        } else {
            val stateListDrawable = StateListDrawable()
            stateListDrawable.addState(
                    intArrayOf( android.R.attr.state_pressed ),
                    ColorDrawable( rippleColor )
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