package studio.forface.demo

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_END
import kotlinx.android.synthetic.main.activity_demo_panels.*
import studio.forface.materialbottombar.demo.R
import studio.forface.materialbottombar.dsl.drawer
import studio.forface.materialbottombar.dsl.panel
import studio.forface.materialbottombar.panels.items.extra.Badge
import studio.forface.materialbottombar.panels.params.*
import studio.forface.materialbottombar.set

private const val IMAGE_URL = "https://i2.wp.com/beebom.com/wp-content/uploads/2016/01/Reverse-Image-Search-Engines-Apps-And-Its-Uses-2016.jpg"

private const val PANEL_SORT_ID = 123
private const val PANEL_SETTINGS_ID = 456

class PanelsDemoActivity: AppCompatActivity() {

    private val adapter = Adapter( words )
    private var settings = Settings()

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo_panels )
        setSupportActionBar( drawerLayout.bottomAppBar )

        setRecyclerView()
        applySettings()

        drawerLayout.drawer = fancyDrawer
        drawerLayout[PANEL_SORT_ID] = sortPanel
        drawerLayout[PANEL_SETTINGS_ID] = settingsPanel
    }

    private val settingsPanel get() = panel {

        header {
            titleText = "Settings"
            titleColor = Color.DKGRAY
            backgroundColor = Color.WHITE
        }

        body {
            allSecondary { selectable = false }
            secondaryItem("Toggle fab alignment" ) {
                iconResource = R.drawable.ic_done_black_24dp
                iconColor = Color.WHITE
                onClick { toggleFabAlignment() }
            }
            secondaryItem("Hide bar on scroll" ) {
                iconResource = if ( settings.hideBarOnScroll ) R.drawable.ic_done_black_24dp
                else R.drawable.ic_clear_black_24dp
                onClick { toggleHideBarOnScroll() }
            }
            secondaryItem("Hide fab on scroll" ) {
                iconResource = if ( settings.hideFabOnScroll ) R.drawable.ic_done_black_24dp
                else R.drawable.ic_clear_black_24dp
                onClick { toggleHideFabOnScroll() }
            }
        }
    }

    private val sortPanel = panel {

        header {
            titleText = "Sort by"
            titleColor = Color.WHITE
            backgroundColor = Color.DKGRAY
        }

        body {
            secondaryItem("Ascending " ) onClick {
                adapter.items = words.sorted()
            }
            secondaryItem("Descending " ) onClick {
                adapter.items = words.sortedDescending()
            }
        }
    }

    private val fancyDrawer = drawer {

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
                        this@PanelsDemoActivity,
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
                    contentText = "Edit"
                    contentColor = Color.RED
                    backgroundColor = Color.RED
                    backgroundCornerRadiusDp = 99f
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

    override fun onCreateOptionsMenu( menu: Menu ): Boolean {
        menuInflater.inflate( R.menu.menu_demo_panens, menu )
        return super.onCreateOptionsMenu( menu )
    }

    override fun onOptionsItemSelected( item: MenuItem ): Boolean {
        when( item.itemId ) {
            R.id.menu_sort -> drawerLayout.openPanel( PANEL_SORT_ID )
            R.id.menu_settings -> drawerLayout.openPanel( PANEL_SETTINGS_ID )
        }
        return super.onOptionsItemSelected( item )
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this )
        recyclerView.adapter = adapter
    }

    private fun toggleFabAlignment() {
        settings = settings.copy( fabAlignment = settings.fabAlignment.other )
        applySettings()
    }

    private fun toggleHideBarOnScroll() {
        settings = settings.copy( hideBarOnScroll = !settings.hideBarOnScroll )
        applySettings( resetSettingsPanel = true )
    }

    private fun toggleHideFabOnScroll() {
        settings = settings.copy( hideFabOnScroll = !settings.hideFabOnScroll )
        applySettings( resetSettingsPanel = true )
    }

    private fun applySettings( resetSettingsPanel: Boolean = false ) {
        bar.fabAlignmentMode = settings.fabAlignment.platformValue
        bar.hideBarOnScroll = settings.hideBarOnScroll
        bar.hideFabOnScroll = settings.hideFabOnScroll
        if ( resetSettingsPanel )
            Handler().postDelayed(200 ) {
                drawerLayout[PANEL_SETTINGS_ID] = settingsPanel
            }
    }
}

private val words = listOf( "dog", "car", "house", "chair", "cat", "spider", "water", "fire",
        "firework", "sea", "summer", "winter", "wind", "console", "personal computer",
        "clothes", "t-shirt", "hammer", "sniper", "camping" )

data class Settings(
        val fabAlignment: FabAlignment = FabAlignment.END,
        val hideBarOnScroll: Boolean = false,
        val hideFabOnScroll: Boolean = false
) {
    enum class FabAlignment( val platformValue: Int ) {
        CENTER( FAB_ALIGNMENT_MODE_CENTER ), END( FAB_ALIGNMENT_MODE_END );
        val other get() = when (CENTER) {
            this -> END
            else -> CENTER
        }
    }
}