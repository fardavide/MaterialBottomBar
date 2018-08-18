package studio.forface.bottomappbar.materialpanels.adapter

import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.drawer_item_base.view.*
import studio.forface.bottomappbar.materialbottomdrawer.drawer.MaterialDrawer
import studio.forface.bottomappbar.materialpanels.panelitems.BasePanelItem
import studio.forface.bottomappbar.materialpanels.panelitems.PanelItem
import studio.forface.bottomappbar.utils.constraintParams
import studio.forface.bottomappbar.utils.dpToPixels

class ItemViewHolder internal constructor(
        itemView: View,
        private val drawerBody: MaterialDrawer.Body
): RecyclerView.ViewHolder( itemView ) {

    fun bind( drawerItem: PanelItem) {

        when( drawerItem ) {
            is BasePanelItem -> {
                itemView.item_icon.alpha = drawerItem.iconAlpha
                itemView.item_icon.constraintParams!!.marginStart =
                        dpToPixels( drawerItem.iconMarginStartDp ).toInt()
                itemView.item_title.constraintParams!!.marginStart =
                        dpToPixels( drawerItem.iconMarginEndDp ).toInt()

                drawerItem.applyTitleTo(    itemView.item_title )
                drawerItem.applyIconTo(     itemView.item_icon,true )

                drawerItem.applyBadgeTo(    itemView.item_badge )
                drawerItem.applyButtonTo(   itemView.item_button )

                drawerBody.applySelectionTo( itemView, drawerItem.selected )

                itemView.isEnabled = drawerItem.selectable
                itemView.setOnClickListener( itemClickListener( drawerItem ) )

            }
        }
    }

    private val itemClickListener: ( BasePanelItem) -> (View) -> Unit get() = { item -> {
        drawerBody.onItemClickListener( item.id, itemView.item_title.text )

        Handler().postDelayed({
            item.selected = true
            drawerBody.setSelected( item.id )
        }, 200)
    } }

}