package studio.forface.bottomappbar.utils

import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ColorDrawable
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.os.Build
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapePathModel


object Drawables {

    fun getSelectableDrawableFor(
            color: Int,
            roundedCorners: Boolean = false
    ): Drawable {
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
                    ColorDrawable( Color.TRANSPARENT )
            )
            return stateListDrawable

        } else {
            val colorList = ColorStateList.valueOf( color )
            val content = ColorDrawable( Color.TRANSPARENT )
            val mask = getRippleColor( color, roundedCorners )
            return RippleDrawable(
                    colorList,
                    content,
                    mask
            )
        }
    }

    fun getRippleColor(
            color: Int, roundedCorners: Boolean, shouldLighten: Boolean = false
    ): Drawable {
        val shapedPathModel = ShapePathModel().apply {
            val corners = if ( roundedCorners ) dpToPixels(12f ) else 0f
            setAllCorners( RoundedCornerTreatment( corners ) )
        }

        return MaterialShapeDrawable( shapedPathModel ).apply {
            setTint( color )
            if ( shouldLighten ) alpha = 50
        }
    }

}