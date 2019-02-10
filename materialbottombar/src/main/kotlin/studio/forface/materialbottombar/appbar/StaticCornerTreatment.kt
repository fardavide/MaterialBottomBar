package studio.forface.materialbottombar.appbar

import com.google.android.material.shape.CornerTreatment

/**
 * @author Davide Giuseppe Farella.
 * An abstract class for a custom [CornerTreatment] that has a fixed interpolator.
 * @see fixedInterpolation
 */
abstract class StaticCornerTreatment( internal val size: Float ) : CornerTreatment() {

    /** A fixed interpolation for the corner radius */
    var fixedInterpolation = 1f
}