package studio.forface.bottomappbar.materialbottomappbar

import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.ShapePath

abstract class StaticCornerTreatment( internal val size: Float ) : CornerTreatment() {

    var fixedInterpolation = 1f

}