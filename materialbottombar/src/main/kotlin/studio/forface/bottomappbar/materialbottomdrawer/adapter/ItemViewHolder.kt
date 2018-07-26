package studio.forface.bottomappbar.materialbottomdrawer.adapter

import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.ripple.RippleUtils
import kotlinx.android.synthetic.main.drawer_item_base.view.*
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.BaseDrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.holders.ColorHolder
import studio.forface.bottomappbar.materialbottomdrawer.params.Selection
import studio.forface.bottomappbar.utils.Drawables
import studio.forface.bottomappbar.utils.constraintParams
import studio.forface.bottomappbar.utils.dpToPixels
import timber.log.Timber

class ItemViewHolder(
        itemView: View, val selection: Selection<*>
): RecyclerView.ViewHolder( itemView ) {

    fun bind( drawerItem: DrawerItem ) {
        when( drawerItem ) {
            is BaseDrawerItem -> {
                itemView.item_icon.alpha = drawerItem.iconAlpha

                itemView.item_icon.constraintParams.marginStart =
                        dpToPixels( drawerItem.iconMarginStartDp ).toInt()
                itemView.item_title.constraintParams.marginStart =
                        dpToPixels( drawerItem.iconMarginEndDp ).toInt()

                drawerItem.applyTitleTo( itemView.item_title )
                drawerItem.applyIconTo( itemView.item_icon )

                selection.applyTo( itemView )

                itemView.setOnClickListener {
                    Toast.makeText(
                            it.context,
                            "${itemView.item_title.text} clicked",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}