package studio.forface.demo

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class Adapter( items: List<String> ): RecyclerView.Adapter<Adapter.ViewHolder>() {

    var items: List<String> = items
        set(value) {
            val oldValue = field
            field = value

            val diffResult = DiffUtil.calculateDiff( DiffCallback( oldValue, value, StringsComparator ) )
            diffResult.dispatchUpdatesTo(this )
        }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ViewHolder {
        val textView = TextView( parent.context ).apply {
            height = 72.dp
            textSize = 20f
            setPadding( 16.dp )
            gravity = Gravity.CENTER_VERTICAL
        }
        return ViewHolder( textView )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder( holder: ViewHolder, position: Int ) {
        holder.bind( items[position] )
    }

    class ViewHolder( itemView: TextView ): RecyclerView.ViewHolder( itemView ) {
        fun bind( item: String ) {
            ( itemView as TextView ).text = item
        }
    }

    /**
     * An object that compare 2 [String]'s for declare whether the [ViewHolder] needs to update the
     * corresponding item.
     *
     * @see Adapter.ItemsComparator
     */
    private object StringsComparator: ItemsComparator<String>() {
        /** @see Adapter.ItemsComparator.areItemsTheSame */
        override fun areItemsTheSame( oldItem: String, newItem: String ) = oldItem == newItem
        /** @see Adapter.ItemsComparator.areContentsTheSame */
        override fun areContentsTheSame( oldItem: String, newItem: String ) = true
    }

    /**
     * A [DiffUtil.Callback] for [Adapter].
     *
     * @param itemsComparator an [ItemsComparator] of [T] for compare old items to new items.
     */
    class DiffCallback<T>(
            private val oldList: List<T>, private val newList: List<T>,
            private val itemsComparator: ItemsComparator<T>
    ) : DiffUtil.Callback() {

        /** @see DiffUtil.Callback.getOldListSize */
        override fun getOldListSize() = oldList.size

        /** @see DiffUtil.Callback.getNewListSize */
        override fun getNewListSize(): Int = newList.size

        /** @see DiffUtil.Callback.areItemsTheSame */
        override fun areItemsTheSame( oldItemPosition: Int, newItemPosition: Int ): Boolean =
                itemsComparator.areItemsTheSame( oldList[oldItemPosition], newList[newItemPosition] )

        /** @see DiffUtil.Callback.areContentsTheSame */
        override fun areContentsTheSame( oldItemPosition: Int, newItemPosition: Int ): Boolean =
                itemsComparator.areContentsTheSame( oldList[oldItemPosition], newList[newItemPosition] )

        /** @see DiffUtil.Callback.getChangePayload */
        override fun getChangePayload( oldItemPosition: Int, newItemPosition: Int ): Any? =
                itemsComparator.getChangePayload( oldList[oldItemPosition], newList[newItemPosition] )
    }

    /**
     * An abstract class for compare two items [T] for declare if they are the same items and
     * if the have the same contents.
     *
     * Used by [DiffCallback].
     */
    abstract class ItemsComparator<T> {
        /**
         * Called by the [DiffCallback.areItemsTheSame] to decide whether two object represent
         * the same Item.
         * <p>
         * For example, if your items have unique ids, this method should check their id equality.
         *
         * @return True if the two items represent the same object or false if they are different.
         */
        abstract fun areItemsTheSame( oldItem: T, newItem: T ): Boolean

        /**
         * Called by the [DiffCallback.areContentsTheSame] when it wants to check whether two
         * items have the same data.
         * [DiffUtil] uses this information to detect if the contents of an item has changed.
         * <p>
         * DiffUtil uses this method to check equality instead of [Object.equals] so that you
         * can change its behavior depending on your UI.
         * For example, if you are using DiffUtil with a [RecyclerView.Adapter], you should
         * return whether the items' visual representations are the same.
         * <p>
         * This method is called only if [areItemsTheSame] returns `true` for these items.
         *
         * @return True if the contents of the items are the same or false if they are different.
         */
        open fun areContentsTheSame( oldItem: T, newItem: T ): Boolean =
                oldItem == newItem

        /**
         * When [areItemsTheSame] returns `true` for two items and [areContentsTheSame] returns
         * false for them, DiffUtil
         * calls this method to get a payload about the change.
         *
         *
         * For example, if you are using DiffUtil with [RecyclerView], you can return the
         * particular field that changed in the item and your [RecyclerView.ItemAnimator] can
         * use that information to run the correct animation.
         *
         *
         * Default implementation returns `null`.
         *
         * @return A payload object that represents the change between the two items.
         */
        open fun getChangePayload( oldItem: T, newItem: T ): Any? = null
    }
}