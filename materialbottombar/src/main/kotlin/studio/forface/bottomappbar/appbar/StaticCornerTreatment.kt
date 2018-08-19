package studio.forface.bottomappbar.appbar

import com.google.android.material.shape.CornerTreatment

abstract class StaticCornerTreatment( internal val size: Float ) : CornerTreatment() {

    var fixedInterpolation = 1f

}