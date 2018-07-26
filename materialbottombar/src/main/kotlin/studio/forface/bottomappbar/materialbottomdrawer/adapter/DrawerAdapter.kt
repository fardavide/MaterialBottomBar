package studio.forface.bottomappbar.materialbottomdrawer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.BaseDrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.Divider
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.materialbottombar.bottomappbar.R

class DrawerAdapter: RecyclerView.Adapter<ItemViewHolder>() {

    internal val items = mutableListOf<DrawerItem>()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ItemViewHolder {
        val view = LayoutInflater.from( parent.context )
                .inflate( getLayoutRes( viewType ), parent,false )
        return ItemViewHolder( view )
    }

    override fun onBindViewHolder( holder: ItemViewHolder, position: Int ) {
        holder.bind( items[position] )
    }

    override fun getItemCount() = items.size

    override fun getItemViewType( position: Int ): Int {
        return position
    }

    private fun getLayoutRes( position: Int ) = when ( items[position] ) {
        is BaseDrawerItem ->    R.layout.drawer_item_base
        is Divider ->           R.layout.drawer_item_divider
        else -> throw NotImplementedError()
    }


}