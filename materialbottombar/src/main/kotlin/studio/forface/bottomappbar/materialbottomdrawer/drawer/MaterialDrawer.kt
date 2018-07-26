package studio.forface.bottomappbar.materialbottomdrawer.drawer

import android.widget.ImageView
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.holders.*
import studio.forface.bottomappbar.materialbottomdrawer.params.Background
import studio.forface.bottomappbar.materialbottomdrawer.params.Icon
import studio.forface.bottomappbar.materialbottomdrawer.params.Selection
import studio.forface.bottomappbar.materialbottomdrawer.params.Title

class MaterialDrawer(
    val header: Header? = null,
    val body: Body? = null
) {

    class Header:
            Background<Header>,
            Title<Header>,
            Icon<Header>
    {
        override val thisRef: Header get() = this

        override var backgroundColorHolder =    ColorHolder()

        override var titleTextHolder =          TextHolder()
        override var titleTextStyleHolder =     TextStyleHolder()
        override var titleTextSizeHolder =      TextSizeHolder()
        override var titleColorHolder =         ColorHolder()

        override var iconImageHolder =          ImageHolder()
        override var iconColorHolder =          ColorHolder()
        override var iconSizeHolder =           IconSizeHolder()

        private var hasCustomShape = false

        override fun withIconShape( imageShape: ImageShape ) {
            super.withIconShape( imageShape )
            hasCustomShape = true
        }

        override fun applyIconTo( imageView: ImageView ) {
            if ( ! hasCustomShape ) iconImageHolder.imageShape = ImageShape.ROUND
            super.applyIconTo( imageView )
        }
    }

    class Body(
        val items: List<DrawerItem> = mutableListOf()
    ): Selection<Body> {
        override val thisRef: Body get() = this

        override var roundedCorners: Boolean = true
        override var selectionColorHolder = ColorHolder()
    }

}