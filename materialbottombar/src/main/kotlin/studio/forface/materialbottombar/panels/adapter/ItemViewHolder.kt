package studio.forface.materialbottombar.panels.adapter

import android.os.Handler
import android.view.View
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.drawer_item_base.view.*
import studio.forface.materialbottombar.panels.AbsMaterialPanel
import studio.forface.materialbottombar.panels.AbsMaterialPanel.BaseBody.Companion.SELECTION_DELAY_MS
import studio.forface.materialbottombar.panels.MaterialPanel
import studio.forface.materialbottombar.panels.items.BasePanelItem
import studio.forface.materialbottombar.panels.items.PanelItem
import studio.forface.materialbottombar.panels.items.extra.ButtonItem
import studio.forface.materialbottombar.utils.constraintParams
import studio.forface.materialbottombar.utils.dpToPixels

/**
 * @author Davide Giuseppe Farella
 * A [RecyclerView.ViewHolder] for the Panel Adapter
 */
open class ItemViewHolder<B: AbsMaterialPanel.BaseBody<*>>(
        itemView: View,
        protected val panelBody: B,
        protected val closePanel: () -> Unit
): RecyclerView.ViewHolder( itemView ) {

    /** @return a [CharSequence] title of the item, read directly from TextView for simplicity */
    protected val title: CharSequence get() = itemView.item_title.text

    /** Bind the given [PanelItem] */
    fun bind( panelItem: PanelItem ) {

        // Only handle BasePanelItem since its supertypes don't need to be bind ( e.g. Divider )
        if ( panelItem is BasePanelItem<*> ) {
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

    /** @return a [View.OnClickListener] for the given [BasePanelItem] */
    protected open val itemClickListener: (BasePanelItem<*>) -> (View) -> Unit get() = { item -> {
        // Trigger the click action to panelBody
        panelBody.onItemClick( item.id, title )
        // Close Panel and set the item selected in panelBody with a delay for let the ripple
        // animation finish first
        Handler().postDelayed( SELECTION_DELAY_MS ) {
            if ( panelBody.closeOnClick ) closePanel()
            item.selected = true
            panelBody.setSelected( item.id )
        }
    } }

    /** @return a [View.OnClickListener] for the given [ButtonItem] */
    private val buttonItemClickListener: (ButtonItem) -> (View) -> Unit get() = { buttonItem -> {
        panelBody.onItemClick( buttonItem.id, itemView.item_button.text )
    } }
}