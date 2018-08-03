package studio.forface.bottomappbar.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Build
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapePathModel


object Drawables {

    const val CORNER_RADIUS_SOFT = 12f

    fun selectableDrawable(
            color: Int,
            cornerRadius: Float
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
            val mask = materialDrawable( color, cornerRadius )
            return RippleDrawable(
                    colorList,
                    content,
                    mask
            )
        }
    }

    fun materialDrawable(
            color: Int, cornerRadius: Float, shouldLighten: Boolean = false
    ): Drawable {
        val shapedPathModel = ShapePathModel().apply {
            setAllEdges( EdgeTreatment() )
            setAllCorners( RoundedCornerTreatment( cornerRadius ) )
        }

        return MaterialShapeDrawable( shapedPathModel ).apply {
            setTint( color )
            if ( shouldLighten ) alpha = 50
        }
    }

}