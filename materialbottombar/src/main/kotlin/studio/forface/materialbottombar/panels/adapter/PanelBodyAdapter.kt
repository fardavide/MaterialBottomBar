package studio.forface.materialbottombar.panels.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studio.forface.materialbottombar.panels.MaterialPanel
import studio.forface.materialbottombar.panels.items.BasePanelItem
import studio.forface.materialbottombar.panels.items.Divider
import studio.forface.materialbottombar.bottomappbar.R

internal class PanelBodyAdapter( val body: MaterialPanel.BaseBody<*> )
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
        is BasePanelItem<*> -> R.layout.drawer_item_base
        is Divider ->          R.layout.drawer_item_divider
        else -> throw NotImplementedError()
    }
}