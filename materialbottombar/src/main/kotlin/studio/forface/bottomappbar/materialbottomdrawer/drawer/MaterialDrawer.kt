package studio.forface.bottomappbar.materialbottomdrawer.drawer

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.holders.*
import studio.forface.bottomappbar.materialbottomdrawer.params.BackgroundColor
import studio.forface.bottomappbar.materialbottomdrawer.params.Icon
import studio.forface.bottomappbar.materialbottomdrawer.params.Title
import java.io.File

class MaterialDrawer(
    val header: Header? = null,
    val items: MutableList<DrawerItem> = mutableListOf()
) {

    class Header:
            BackgroundColor<Header>,
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
    }

}