package studio.forface.bottomappbar.materialbottomdrawer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.materialbottombar.bottomappbar.R

class DrawerAdapter: RecyclerView.Adapter<ItemViewHolder>() {

    internal val items = mutableListOf<DrawerItem>()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ItemViewHolder {
        val view = LayoutInflater.from( parent.context )
                .inflate( R.layout.drawer_item, parent,false )
        return ItemViewHolder( view )
    }

    override fun onBindViewHolder( holder: ItemViewHolder, position: Int ) {
        holder.bind( items[position] )
    }

    override fun getItemCount() = items.size

}