package studio.forface.demo

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_END
import kotlinx.android.synthetic.main.activity_demo.*
import studio.forface.bottomappbar.drawer.MaterialDrawer
import studio.forface.bottomappbar.drawer.items.PrimaryDrawerItem
import studio.forface.bottomappbar.drawer.items.SecondaryDrawerItem
import studio.forface.bottomappbar.dsl.drawer
import studio.forface.bottomappbar.dsl.panel
import studio.forface.bottomappbar.panels.items.Divider
import studio.forface.bottomappbar.panels.items.extra.Badge
import studio.forface.bottomappbar.panels.params.*
import studio.forface.materialbottombar.demo.R
import timber.log.Timber

private const val IMAGE_URL = "https://i2.wp.com/beebom.com/wp-content/uploads/2016/01/Reverse-Image-Search-Engines-Apps-And-Its-Uses-2016.jpg"

private const val PANEL_SORT_ID = 123

class DemoActivity: AppCompatActivity() {

    override fun onCreate( savedInstanceState: Bundle? ) {
        Timber.plant( Timber.DebugTree() )
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo )
        setSupportActionBar( drawerLayout.bottomAppBar )

        setRecyclerView()
        setButtons()

        drawerLayout.drawer = fancyDrawer

        val panelSort = panel {

            header {
                titleText = "Sort by"
                titleColor = Color.WHITE
                backgroundColor = Color.DKGRAY
            }

            body {
                primaryItem("Name ") { id = 100 }
                primaryItem("Surname ") { id = 101 }
            }
        }

        drawerLayout.addPanel( panelSort, PANEL_SORT_ID )

        drawerLayout.postDelayed( { drawerLayout.bottomAppBar?.hideAndShow() },1000 )
        drawerLayout.postDelayed( { drawerLayout.hideAndShowToolbar() },1000 )
    }

    override fun onCreateOptionsMenu( menu: Menu ): Boolean {
        menuInflater.inflate( R.menu.menu_demo, menu )
        return super.onCreateOptionsMenu( menu )
    }

    override fun onOptionsItemSelected( item: MenuItem ): Boolean {
        when( item.itemId ) {
            R.id.app_bar_sort -> drawerLayout.openPanel( PANEL_SORT_ID )
        }
        return super.onOptionsItemSelected( item )
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this )
        recyclerView.adapter = Adapter()
    }

    private fun setButtons() {
        val active = Color.parseColor("#30D5C8" )
        val inactive = Color.GRAY

        switchFab.setOnClickListener {
            bar.fabAlignmentMode = if ( bar.fabAlignmentMode == FAB_ALIGNMENT_MODE_CENTER )
                FAB_ALIGNMENT_MODE_END else FAB_ALIGNMENT_MODE_CENTER
        }

        hideFab.setOnClickListener {
            val ( bg, set ) = if ( bar.hideFabOnScroll )
                inactive to false
            else active to true

            hideFab.setBackgroundColor( bg )
            bar.hideFabOnScroll = set
        }

        hideBar.setOnClickListener {
            val ( bg, set ) = if ( bar.hideBarOnScroll )
                inactive to false
            else active to true

            hideBar.setBackgroundColor( bg )
            bar.hideBarOnScroll = set
        }

    }

    private val testDrawer: MaterialDrawer get() {
        val header = MaterialDrawer.Header()
                .iconUrl( IMAGE_URL )
                .backgroundColor( Color.RED )
                .titleText("Test drawer" )
                .titleColor( Color.WHITE )
                .titleBold()

        val chat = PrimaryDrawerItem()
                .titleText("Messages" )
                .titleSpSize(10f )
                .titleColor( Color.MAGENTA )
                .iconResource( R.drawable.ic_message_black_24dp )
                .id(1 )

                .badgeBackgroundColor( Color.CYAN )
                .badgeBackgroundCornerRadiusDp(999f )
                .badgeContentText("Banana" )
                .badgeContentBold()
                .badgeContentColor( Color.BLACK )

                .buttonContentText("Edit" )
                .buttonContentColor( Color.BLUE )
                .buttonBackgroundColor( Color.BLUE )
                .buttonBackgroundCornerRadiusDp(999f )
                .buttonStyle( ButtonStyle.FLAT )

        val inbox = PrimaryDrawerItem()
                .titleText("Inbox" )
                .titleBold()
                .iconResource( R.drawable.ic_inbox_black_24dp )
                .id(2 )

                .badgeBackgroundColor( Color.TRANSPARENT )
                .badgeBackgroundCornerRadiusDp(999f )
                .badgeContentText("Banana" )
                .badgeContentSpSize(14f )
                .badgeContentColor( Color.GREEN )

                .buttonContentText("Edit" )
                .buttonContentColor( Color.BLACK )
                .buttonContentSpSize( 8f )
                .buttonContentBold()
                .buttonBackgroundColor( Color.RED )
                .buttonBackgroundCornerRadiusDp(10f )
                .buttonStyle( ButtonStyle.FLAT )

        val work = SecondaryDrawerItem()
                .titleText("Work" )
                .iconResource( R.drawable.ic_work_black_24dp )
                .id(3 )

                .badgeBackgroundColor( Color.BLUE )
                .badgeBackgroundCornerRadiusDp(999f )
                .badgeContentText("3" )
                .badgeContentSpSize(12f )
                .badgeContentColor( Color.WHITE )

                .buttonContentText("Edit" )
                .buttonContentColor( Color.WHITE )
                .buttonContentSpSize( 8f )
                .buttonContentBold()
                .buttonBackgroundColor( Color.RED )
                .buttonBackgroundCornerRadiusDp(0f )
                .buttonStyle( ButtonStyle.COLOR )

        val contacts = PrimaryDrawerItem()
                .titleText("Contacts" )
                .titleBold()
                .id(4 )

                .selectable(false )

                .badgeBackgroundColor( Color.BLUE )
                .badgeBackgroundCornerRadiusDp(0f )
                .badgeContentText("3" )
                .badgeContentBold()
                .badgeContentColor( Color.WHITE )

        val favorites = SecondaryDrawerItem()
                .titleText("Favorites" )
                .iconResource( R.drawable.ic_star_black_24dp )
                .id(5 )

        val labels = PrimaryDrawerItem()
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
        val label2 = SecondaryDrawerItem()
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
                .selectionCornerRadiusDp(16f )
                .items( listOf(
                        chat, inbox, work,
                        Divider(),
                        contacts, favorites,
                        Divider(),
                        labels, label1, label2, label3, label4, label5
                ) )
                .setSelected( 5 )
                .itemClickListener { id, title ->
                    Toast.makeText(this, "$title - $id clicked", Toast.LENGTH_SHORT ).show()
                }

        return MaterialDrawer( header, body )
    }

    private val fancyDrawer get() = drawer {

        header {
            iconUrl = IMAGE_URL
            backgroundColorHex = "#30D5C8"
            titleText = "Material drawer"
            titleColor = Color.WHITE
            titleBold = true
        }

        body {

            /* SETUP */
            selectedItem = 1
            selectionColor = Color.RED
            selectionCornerRadiusDp = 16f
            onItemClick = { id, title ->
                Toast.makeText(
                        this@DemoActivity,
                        "$title - $id clicked",
                        Toast.LENGTH_SHORT
                ).show()
            }
            val baseBadge = Badge {
                backgroundColor = Color.RED
                backgroundCornerRadiusDp = 999f
                contentBold = true
                contentColor = Color.WHITE
            }
            allPrimary {
                titleBold = true
                badgeItem = baseBadge
            }
            allSecondary { badgeItem = baseBadge }

            /* ITEMS */
            primaryItem("Messages" ) {
                iconResource = R.drawable.ic_message_black_24dp
                id = 1
                badgeContentText = "New"
            }
            secondaryItem("Inbox" ) {
                iconResource = R.drawable.ic_message_black_24dp
                id = 2
                badgeContentText = "8"
            }

            primaryItem("Work" ) {
                iconResource = R.drawable.ic_work_black_24dp
                id = 3
            }

            divider()

            primaryItem("Contacts" ) {
                iconResource = R.drawable.ic_contacts_black_24dp
                id = 4
            }
            secondaryItem("Favorites" ) {
                iconResource = R.drawable.ic_star_black_24dp
                id = 5
            }

            divider()

            primaryItem("Labels" ) {
                selectable = false
                iconResource = R.drawable.ic_style_black_24dp
                id = 6
                button {
                    buttonContentText = "Edit"
                    buttonContentColor = Color.RED
                    buttonBackgroundColor = Color.RED
                    buttonBackgroundCornerRadiusDp = 99f
                    buttonStyle = ButtonStyle.FLAT
                }
            }
            allSecondary { iconResource = R.drawable.ic_label_black_24dp }
            secondaryItem("Label 1" ) {
                iconColor = Color.RED
                id = 7
            }
            secondaryItem("Label 2" ) {
                iconColor = Color.GREEN
                id = 8
            }
            secondaryItem("Label 3" ) {
                iconColor = Color.BLUE
                id = 9
            }
            secondaryItem("Label 4" ) {
                iconColor = Color.MAGENTA
                id = 10
            }
            secondaryItem("Label 5" ) {
                iconColor = Color.CYAN
                id = 11
            }
        }
    }

    private val oldFancyDrawer: MaterialDrawer get() {
        val header = MaterialDrawer.Header()
                .iconUrl( IMAGE_URL )
                .backgroundColorHex("#30D5C8" )
                .titleText("Material drawer" )
                .titleColor( Color.WHITE )
                .titleBold()

        class MyPrimaryItem: PrimaryDrawerItem() { init {
            titleBold()
        } }

        val chat = MyPrimaryItem()
                .titleText("Messages" )
                .iconResource( R.drawable.ic_message_black_24dp )
                .id(1 )
                .badgeBackgroundColor( Color.RED )
                .badgeBackgroundCornerRadiusDp(999f )
                .badgeContentText("New" )
                .badgeContentBold()
                .badgeContentColor( Color.WHITE )
        val inbox = SecondaryDrawerItem()
                .titleText("Inbox" )
                .iconResource( R.drawable.ic_inbox_black_24dp )
                .id(2 )
                .badgeBackgroundColor( Color.RED )
                .badgeBackgroundCornerRadiusDp(999f )
                .badgeContentText("8" )
                .badgeContentBold()
                .badgeContentColor( Color.WHITE )

        val work = SecondaryDrawerItem()
                .titleText("Work" )
                .iconResource( R.drawable.ic_work_black_24dp )
                .id(3 )

        val contacts = MyPrimaryItem()
                .titleText("Contacts" )
                .iconResource( R.drawable.ic_contacts_black_24dp )
                .id(4 )
        val favorites = SecondaryDrawerItem()
                .titleText("Favorites" )
                .iconResource( R.drawable.ic_star_black_24dp )
                .id(5 )

        val labels = MyPrimaryItem()
                .titleText("Labels" )
                .iconResource( R.drawable.ic_style_black_24dp )
                .id(6 )
                .selectable(false )

                .buttonContentText("Edit" )
                .buttonContentColor( Color.RED )
                .buttonBackgroundColor( Color.RED )
                .buttonBackgroundCornerRadiusDp(99f )
                .buttonStyle( ButtonStyle.FLAT )
        val label1 = SecondaryDrawerItem()
                .titleText("Label 1" )
                .iconResource( R.drawable.ic_label_black_24dp )
                .iconColor( Color.RED )
                .id(7 )
        val label2 = SecondaryDrawerItem()
                .titleText("Label 2" )
                .iconResource( R.drawable.ic_label_black_24dp )
                .iconColor( Color.GREEN )
                .id(8 )
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
                .selectionColor( Color.RED )
                .selectionCornerRadiusDp(16f )
                .items( listOf(
                        chat, inbox, work,
                        Divider(),
                        contacts, favorites,
                        Divider(),
                        labels, label1, label2, label3, label4, label5
                ) )
                .setSelected( 1 )
                .itemClickListener { id, title ->
                    Toast.makeText(this, "$title - $id clicked", Toast.LENGTH_SHORT ).show()
                }

        return MaterialDrawer( header, body )
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