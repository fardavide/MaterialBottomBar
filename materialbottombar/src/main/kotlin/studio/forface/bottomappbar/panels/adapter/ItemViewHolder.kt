package studio.forface.bottomappbar.panels.adapter

import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.drawer_item_base.view.*
import studio.forface.bottomappbar.panels.MaterialPanel
import studio.forface.bottomappbar.panels.items.BasePanelItem
import studio.forface.bottomappbar.panels.items.PanelItem
import studio.forface.bottomappbar.panels.items.extra.ButtonItem
import studio.forface.bottomappbar.utils.constraintParams
import studio.forface.bottomappbar.utils.dpToPixels

class ItemViewHolder internal constructor(
        itemView: View,
        private val panelBody: MaterialPanel.BaseBody<*>
): RecyclerView.ViewHolder( itemView ) {

    fun bind(panelItem: PanelItem ) {

        when( panelItem ) {
            is BasePanelItem -> {
                itemView.item_icon.alpha = panelItem.iconAlpha
                itemView.item_icon.constraintParams!!.marginStart =
                        dpToPixels( panelItem.iconMarginStartDp ).toInt()
                itemView.item_title.constraintParams!!.marginStart =
                        dpToPixels( panelItem.iconMarginEndDp ).toInt()

                panelItem.applyTitleTo(    itemView.item_title )
                panelItem.applyIconTo(     itemView.item_icon,true )

                panelItem.applyBadgeTo(    itemView.item_badge )
                panelItem.applyButtonTo(   itemView.item_button )

                panelBody.applySelectionTo( itemView, panelItem.selected )

                itemView.isEnabled = panelItem.selectable
                itemView.setOnClickListener( itemClickListener( panelItem ) )
                itemView.item_button.setOnClickListener( buttonItemClickListener( panelItem.buttonItem ) )
            }
        }
    }

    private val itemClickListener: (BasePanelItem) -> (View) -> Unit get() = { item -> {
        panelBody.onItemClickListener( item.id, itemView.item_title.text )

        Handler().postDelayed({
            item.selected = true
            panelBody.setSelected( item.id )
        }, 200)
    } }

    private val buttonItemClickListener: (ButtonItem) -> (View) -> Unit get() = { buttonItem -> {
        panelBody.onItemClickListener( buttonItem.id, itemView.item_button.text )
    } }

}