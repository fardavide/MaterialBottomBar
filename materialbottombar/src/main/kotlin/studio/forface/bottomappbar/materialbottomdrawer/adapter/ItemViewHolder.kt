package studio.forface.bottomappbar.materialbottomdrawer.adapter

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.drawer_item_base.view.*
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.BaseDrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.bottomappbar.utils.constraintParams
import studio.forface.bottomappbar.utils.dpToPixels

class ItemViewHolder( itemView: View): RecyclerView.ViewHolder( itemView ) {

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
            }
        }
    }

}