package studio.forface.materialbottombar.panels.adapter

import android.os.Handler
import android.view.View
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.drawer_item_base.view.*
import studio.forface.materialbottombar.panels.AbsMaterialPanel
import studio.forface.materialbottombar.panels.MaterialPanel
import studio.forface.materialbottombar.panels.items.BasePanelItem
import studio.forface.materialbottombar.panels.items.PanelItem
import studio.forface.materialbottombar.panels.items.extra.ButtonItem
import studio.forface.materialbottombar.utils.constraintParams
import studio.forface.materialbottombar.utils.dpToPixels

open class ItemViewHolder<B: AbsMaterialPanel.BaseBody<*>>(
        itemView: View,
        protected val panelBody: B
): RecyclerView.ViewHolder( itemView ) {

    protected val title: CharSequence get() = itemView.item_title.text

    fun bind( panelItem: PanelItem ) {

        when( panelItem ) {
            is BasePanelItem<*> -> {
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
                itemView.item_button
                        .setOnClickListener( buttonItemClickListener( panelItem.buttonItem ) )
            }
        }
    }

    protected open val itemClickListener: (BasePanelItem<*>) -> (View) -> Unit get() = { item -> {
        panelBody.onItemClick( item.id, title )

        Handler().postDelayed(200 ) {
            item.selected = true
            panelBody.setSelected( item.id )
        }
    } }

    private val buttonItemClickListener: (ButtonItem) -> (View) -> Unit get() = { buttonItem -> {
        panelBody.onItemClick( buttonItem.id, itemView.item_button.text )
    } }
}