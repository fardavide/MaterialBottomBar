package studio.forface.bottomappbar.materialbottomdrawer.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.drawer_item.view.*
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.PrimaryDrawerItem

class ItemViewHolder( itemView: View): RecyclerView.ViewHolder( itemView ) {

    fun bind( drawerItem: DrawerItem ) {
        when( drawerItem ) {
            is PrimaryDrawerItem -> {
                itemView.item_icon.alpha = 0.7f

                drawerItem.title?.applyTo( itemView.item_title )
                drawerItem.icon?.applyTo( itemView.item_icon )
            }
        }
    }

}