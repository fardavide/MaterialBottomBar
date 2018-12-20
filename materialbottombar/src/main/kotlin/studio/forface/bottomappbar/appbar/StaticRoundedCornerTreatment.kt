package studio.forface.bottomappbar.appbar

import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapePath

/**
 * @author Davide Giuseppe Farella.
 * A custom version of [RoundedCornerTreatment] that has a fixed interpolator.
 *
 * Inherit from [StaticCornerTreatment].
 * @see RoundedCornerTreatment
 */
class StaticRoundedCornerTreatment( size: Float ) : StaticCornerTreatment( size ) {

    /** @see RoundedCornerTreatment.getCornerPath */
    override fun getCornerPath( angle: Float, interpolation: Float, shapePath: ShapePath ) {
        val radius = size
        shapePath.reset(
                0f,
                radius * fixedInterpolation,
                180f /*ShapePath.ANGLE_LEFT*/,
                180 - angle
        )
        shapePath.addArc(
                0f,
                0f,
                2f * radius * fixedInterpolation,
                2f * radius * fixedInterpolation,
                180f,
                angle
        )
    }
}