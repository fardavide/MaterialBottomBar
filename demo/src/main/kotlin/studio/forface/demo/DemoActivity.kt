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
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.PrimaryDrawerItem
import studio.forface.bottomappbar.utils.times
import studio.forface.materialbottombar.demo.R
import timber.log.Timber
import java.util.*

private const val IMAGE_URL = "https://i2.wp.com/beebom.com/wp-content/uploads/2016/01/Reverse-Image-Search-Engines-Apps-And-Its-Uses-2016.jpg"


class DemoActivity: AppCompatActivity() {

    private val TEXTS = arrayOf(
            "ciao", "wow", "super", "yeah", "yo"
    )

    private val DRAWABLES = arrayOf(
            R.drawable.ic_favorite_black_24dp,
            R.drawable.ic_invert_colors_black_24dp,
            R.drawable.ic_local_pharmacy_black_24dp,
            R.drawable.ic_memory_black_24dp,
            R.drawable.ic_sentiment_very_dissatisfied_black_24dp
    )

    override fun onCreate( savedInstanceState: Bundle? ) {
        Timber.plant( Timber.DebugTree() )
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo )

        setRecyclerView()
        setButtons()

        val icon = MaterialDrawer.Icon()
                .withUrl( IMAGE_URL )
        val title = MaterialDrawer.Title()
                .withText("Test")
                .withColor( Color.WHITE )
        val backgroundColor = MaterialDrawer.BackgroundColor()
                .withColor( Color.RED )

        fun randomItem() = PrimaryDrawerItem(
                MaterialDrawer.Icon().withResource( DRAWABLES[Random().nextInt(5 )] ),
                MaterialDrawer.Title().withText( TEXTS[Random().nextInt(5 )] )
        )

        val items = arrayOfNulls<PrimaryDrawerItem>( 30 )
                .map { randomItem() }

        val header = MaterialDrawer.Header( icon, title, backgroundColor )

        drawerLayout.drawer = MaterialDrawer( header, items.toMutableList() )
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