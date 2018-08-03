package studio.forface.demo

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_demo.*
import studio.forface.bottomappbar.materialbottomdrawer.drawer.MaterialDrawer
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.Divider
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.PrimaryDrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.SecondaryDrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.extra.BadgeItem
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
                .iconUrl( IMAGE_URL )
                .backgroundColor( Color.RED )
                .titleText("My drawer" )
                .titleColor( Color.WHITE )
                .titleBold()

        val badgeItem = BadgeItem()
                .contentColor( Color.RED )

        class MyPrimaryDrawerItem: PrimaryDrawerItem() { init {
            titleBold()
            badgeItem( badgeItem )
        } }

        class MySecondaryDrawerItem: SecondaryDrawerItem() { init {
            badgeItem( badgeItem )
        } }

        val chat = MyPrimaryDrawerItem()
                .titleText("Messages" )
                .iconResource( R.drawable.ic_message_black_24dp )
                .id(1 )

                .badgeContentText("Banana" )

                .buttonContentText("Options" )
                .buttonContentColor( Color.WHITE )
                .buttonBackgroundColor( Color.MAGENTA )
        val inbox = SecondaryDrawerItem()
                .titleText("Inbox" )
                .iconResource( R.drawable.ic_inbox_black_24dp )
                .id(2 )
        val work = SecondaryDrawerItem()
                .titleText("Work" )
                .iconResource( R.drawable.ic_work_black_24dp )
                .id(3 )
                .badgeContentText("3" )

        val contacts = MyPrimaryDrawerItem()
                .titleText("Contacts" )
                .iconResource( R.drawable.ic_contacts_black_24dp )
                .id(4 )
        val favorites = SecondaryDrawerItem()
                .titleText("Favorites" )
                .iconResource( R.drawable.ic_star_black_24dp )
                .id(5 )

        val labels = MyPrimaryDrawerItem()
                .titleText("Labels" )
                .iconResource( R.drawable.ic_style_black_24dp )
                .titleBold()
                .id(6 )
                .badgeContentText("12" )
        val label1 = SecondaryDrawerItem()
                .titleText("Label 1" )
                .iconResource( R.drawable.ic_label_black_24dp )
                .iconColor( Color.RED )
                .id(7 )
        val label2 = MySecondaryDrawerItem()
                .titleText("Label 2" )
                .iconResource( R.drawable.ic_label_black_24dp )
                .iconColor( Color.GREEN )
                .id(8 )
                .badgeContentText("12" )
        val label3 = SecondaryDrawerItem()
                .titleText("Label 3" )
                .iconResource( R.drawable.ic_label_black_24dp )
                .iconColor( Color.BLUE )
                .id(9 )
        val label4 = SecondaryDrawerItem()
                .titleText("Label 4" )
                .iconResource( R.drawable.ic_label_black_24dp )
                .iconColor( Color.MAGENTA )
                .id(10 )
        val label5 = SecondaryDrawerItem()
                .titleText("Label 5" )
                .iconResource( R.drawable.ic_label_black_24dp )
                .iconColor( Color.CYAN )
                .id(11 )

        val body = MaterialDrawer.Body()
                .selectionColor( Color.BLUE )
                .itemClickListener { id, title ->
                    Toast.makeText(this, "$title - $id clicked", Toast.LENGTH_SHORT ).show()
                }

        val drawer = MaterialDrawer()
        drawerLayout.drawer = drawer

        // Testing postponed changes
        drawer.header = header
        drawer.body = body

        drawer.body?.apply { items = listOf(
                chat, inbox, work,
                Divider(),
                contacts, favorites,
                Divider(),
                labels, label1, label2, label3, label4, label5
        ) }

        drawer.body!!.setSelected( 5 )
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