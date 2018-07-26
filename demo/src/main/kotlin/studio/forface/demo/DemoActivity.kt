package studio.forface.demo

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_demo.*
import studio.forface.bottomappbar.materialbottomdrawer.drawer.MaterialDrawer
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.Divider
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.PrimaryDrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.SecondaryDrawerItem
import studio.forface.materialbottombar.demo.R
import timber.log.Timber

private const val IMAGE_URL = "https://i2.wp.com/beebom.com/wp-content/uploads/2016/01/Reverse-Image-Search-Engines-Apps-And-Its-Uses-2016.jpg"


class DemoActivity: AppCompatActivity() {

    override fun onCreate( savedInstanceState: Bundle? ) {
        Timber.plant( Timber.DebugTree() )
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo )

        setRecyclerView()
        setButtons()

        val header = MaterialDrawer.Header()
                .withIconUrl( IMAGE_URL )
                .withBackgroundColor( Color.RED )
                .withTitleText("My drawer" )
                .withTitleColor( Color.WHITE )
                .withTitleBold()

        val chat = PrimaryDrawerItem()
                .withTitleText("Messages" )
                .withIconResource( R.drawable.ic_message_black_24dp )
                .withTitleBold()
        val inbox = SecondaryDrawerItem()
                .withTitleText("Inbox" )
                .withIconResource( R.drawable.ic_inbox_black_24dp )
        val work = SecondaryDrawerItem()
                .withTitleText("Work" )
                .withIconResource( R.drawable.ic_work_black_24dp )

        val contacts = PrimaryDrawerItem()
                .withTitleText("Contacts" )
                .withIconResource( R.drawable.ic_contacts_black_24dp )
                .withTitleBold()
        val favorites = SecondaryDrawerItem()
                .withTitleText("Favorites" )
                .withIconResource( R.drawable.ic_star_black_24dp )

        val labels = PrimaryDrawerItem()
                .withTitleText("Labels" )
                .withIconResource( R.drawable.ic_style_black_24dp )
                .withTitleBold()
        val label1 = SecondaryDrawerItem()
                .withTitleText("Label 1" )
                .withIconResource( R.drawable.ic_label_black_24dp )
                .withIconColor( Color.RED )
        val label2 = SecondaryDrawerItem()
                .withTitleText("Label 2" )
                .withIconResource( R.drawable.ic_label_black_24dp )
                .withIconColor( Color.GREEN )
        val label3 = SecondaryDrawerItem()
                .withTitleText("Label 3" )
                .withIconResource( R.drawable.ic_label_black_24dp )
                .withIconColor( Color.BLUE )
        val label4 = SecondaryDrawerItem()
                .withTitleText("Label 4" )
                .withIconResource( R.drawable.ic_label_black_24dp )
                .withIconColor( Color.MAGENTA )
        val label5 = SecondaryDrawerItem()
                .withTitleText("Label 5" )
                .withIconResource( R.drawable.ic_label_black_24dp )
                .withIconColor( Color.CYAN )

        val body = MaterialDrawer.Body( listOf(
                chat, inbox, work,
                Divider(),
                contacts, favorites,
                Divider(),
                labels, label1, label2, label3, label4, label5
        ) ).withSelectionColor( Color.RED )

        drawerLayout.drawer = MaterialDrawer( header, body )
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this )
        recyclerView.adapter = Adapter()
    }

    private fun setButtons() {
        val active = Color.parseColor("#30D5C8" )
        val inactive = Color.GRAY

        switchFab.setOnClickListener {
            bar.fabAlignmentMode = if ( bar.fabAlignmentMode == 0 ) 1 else 0
        }

        hideFab.setOnClickListener {
            val ( bg, set ) = if ( bar.hideFabOnScroll )
                inactive to false
            else active to true

            hideFab.background.setColorFilter( bg, PorterDuff.Mode.SRC )
            bar.hideFabOnScroll = set
        }

        hideBar.setOnClickListener {
            val ( bg, set ) = if ( bar.hideBarOnScroll )
                inactive to false
            else active to true

            hideBar.background.setColorFilter( bg, PorterDuff.Mode.SRC )
            bar.hideBarOnScroll = set
        }

    }

}

class Adapter: RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ViewHolder {
        val textView = TextView( parent.context ).apply {
            height = 150
            textSize = 20f
        }
        return ViewHolder( textView )
    }

    override fun getItemCount() = 500

    override fun onBindViewHolder( holder: ViewHolder, position: Int ) {
        holder.bind( position )
    }

    class ViewHolder( itemView: TextView ): RecyclerView.ViewHolder( itemView ) {
        fun bind( position: Int ) {
            val text = "Item #$position"
            ( itemView as TextView ).text = text
        }
    }
}