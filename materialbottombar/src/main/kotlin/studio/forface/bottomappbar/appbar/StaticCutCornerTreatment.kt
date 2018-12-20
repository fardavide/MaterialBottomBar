package studio.forface.bottomappbar.appbar

import com.google.android.material.shape.CutCornerTreatment
import com.google.android.material.shape.ShapePath

/**
 * @author Davide Giuseppe Farella.
 * A custom version of [CutCornerTreatment] that has a fixed interpolator.
 *
 * Inherit from [StaticCornerTreatment].
 * @see CutCornerTreatment
 */
class StaticCutCornerTreatment( size: Float) : StaticCornerTreatment( size ) {

    /** @see CutCornerTreatment.getCornerPath */
    override fun getCornerPath( angle: Float, interpolation: Float, shapePath: ShapePath ) {
        val radius = size
        shapePath.reset(
                0f,
                radius * fixedInterpolation,
                180f /* ShapePath.ANGLE_LEFT */,
                180 - angle
        )
        shapePath.lineTo(
                ( Math.sin( Math.toRadians( angle.toDouble() ) ) * radius.toDouble() * fixedInterpolation.toDouble() ).toFloat(),
                // Something about using cos() is causing rounding which prevents the path from being convex
                // on api levels 21 and 22. Using sin() with 90 - angle is helping for now.
                ( Math.sin( Math.toRadians( ( 90 - angle ).toDouble() ) ) * radius.toDouble() * fixedInterpolation.toDouble() ).toFloat()
        )
    }
}