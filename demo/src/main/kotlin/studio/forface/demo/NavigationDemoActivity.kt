package studio.forface.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_demo_panels.*
import studio.forface.materialbottombar.demo.NavGraphDirections.Companion.actionToEpgsFragment
import studio.forface.materialbottombar.demo.NavGraphDirections.Companion.actionToMoviesFragment
import studio.forface.materialbottombar.demo.NavGraphDirections.Companion.actionToPlaylistFragment
import studio.forface.materialbottombar.demo.NavGraphDirections.Companion.actionToSettingsFragment
import studio.forface.materialbottombar.demo.NavGraphDirections.Companion.actionToTvsFragment
import studio.forface.materialbottombar.demo.R
import studio.forface.materialbottombar.navigation.dsl.navDrawer
import studio.forface.materialbottombar.panels.params.*

class NavigationDemoActivity: AppCompatActivity() {

    private val navController by lazy { findNavController( R.id.nav_host ) }

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo_navigation )
        setSupportActionBar( drawerLayout.bottomAppBar )

        drawerLayout.drawer = navDrawer ( navController ) {
            header {
                titleTextRes = R.string.app_name
                titleBold = true
            }
            body {
                allPrimary {
                    titleBold = false
                    titleSpSize = 14f
                    iconDpSize = 36f
                }

                // Tv Channels
                primaryItem( R.string.menu_tv_channels ) {
                    iconResource = R.drawable.ic_tv
                    navDirections = actionToTvsFragment()
                }

                // Movie Channels
                primaryItem( R.string.menu_movie_channels ) {
                    iconResource = R.drawable.ic_movie
                    navDirections = actionToMoviesFragment()
                }

                // Divider
                divider()

                // My Playlists
                primaryItem( R.string.menu_my_playlists ) {
                    iconResource = R.drawable.ic_playlist
                    navDirections = actionToPlaylistFragment()
                }

                // My EPGs
                primaryItem( R.string.menu_my_epgs ) {
                    iconResource = R.drawable.ic_epg
                    navDirections = actionToEpgsFragment()
                }

                // Divider
                divider()

                // Settings
                primaryItem( R.string.menu_settings ) {
                    iconResource = R.drawable.ic_settings
                    navDestinationId = R.id.action_to_SettingsFragment
                }
            }
        }
    }

    override fun onNavigateUp() = navController.navigateUp() || false.also { onBackPressed() }
}