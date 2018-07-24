package studio.forface.bottomappbar.materialbottomappbar

import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.ShapePath

class StaticCutCornerTreatment( size: Float) : StaticCornerTreatment( size ) {

    override fun getCornerPath( angle: Float, interpolation: Float, shapePath: ShapePath ) {
        shapePath.reset(0.0f, this.size * fixedInterpolation)
        shapePath.lineTo(
                ( Math.sin(angle.toDouble() ) * size.toDouble() * fixedInterpolation.toDouble() ).toFloat(),
                ( Math.cos(angle.toDouble() ) * this.size.toDouble() * fixedInterpolation.toDouble() ).toFloat()
        )
    }
}