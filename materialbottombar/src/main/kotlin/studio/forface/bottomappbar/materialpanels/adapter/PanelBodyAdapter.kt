package studio.forface.bottomappbar.materialpanels.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studio.forface.bottomappbar.materialbottomdrawer.drawer.MaterialDrawer
import studio.forface.bottomappbar.materialpanels.panelitems.BasePanelItem
import studio.forface.bottomappbar.materialpanels.panelitems.Divider
import studio.forface.materialbottombar.bottomappbar.R

internal class PanelBodyAdapter( val body: MaterialDrawer.Body )
    : RecyclerView.Adapter<ItemViewHolder>() {

    val items get() = body.items

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ItemViewHolder {
        val view = LayoutInflater.from( parent.context )
                .inflate( getLayoutRes( viewType ), parent,false )
        return ItemViewHolder( view, body )
    }

    override fun onBindViewHolder( holder: ItemViewHolder, position: Int ) {
        holder.bind( items[position] )
    }

    override fun getItemCount() = items.size

    override fun getItemViewType( position: Int ): Int {
        return position
    }

    private fun getLayoutRes( position: Int ) = when ( items[position] ) {
        is BasePanelItem ->    R.layout.drawer_item_base
        is Divider ->          R.layout.drawer_item_divider
        else -> throw NotImplementedError()
    }


}