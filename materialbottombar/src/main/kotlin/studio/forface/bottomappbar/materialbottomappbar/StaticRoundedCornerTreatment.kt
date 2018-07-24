package studio.forface.bottomappbar.materialbottomappbar

import com.google.android.material.shape.ShapePath

class StaticRoundedCornerTreatment( size: Float ) : StaticCornerTreatment( size ) {

    override fun getCornerPath( angle: Float, interpolation: Float, shapePath: ShapePath ) {
        shapePath.reset(0.0f, this.size * fixedInterpolation )
        shapePath.addArc(
                0.0f,0.0f,
                2.0f * this.size * fixedInterpolation,
                2.0f * this.size * fixedInterpolation,
                angle + 180.0f, 90.0f)
    }
}