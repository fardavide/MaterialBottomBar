package studio.forface.bottomappbar.materialbottomdrawer.adapter

import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.drawer_item_base.view.*
import studio.forface.bottomappbar.materialbottomdrawer.drawer.MaterialDrawer
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.BaseDrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.bottomappbar.utils.constraintParams
import studio.forface.bottomappbar.utils.dpToPixels

class ItemViewHolder internal constructor(
        itemView: View,
        private val drawerBody: MaterialDrawer.Body
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
                drawerBody.applySelectionTo( itemView, drawerItem.selected )

                itemView.isEnabled = drawerItem.selectable

                if ( drawerItem.selectable ) {
                    itemView.setOnClickListener {
                        drawerBody.onItemClickListener(drawerItem.id, itemView.item_title.text)

                        Handler().postDelayed({
                            drawerItem.selected = true
                            drawerBody.setSelected(drawerItem.id)
                        }, 200)
                    }

                }
            }
        }
    }

}