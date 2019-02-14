package studio.forface.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import studio.forface.materialbottombar.demo.R

abstract class BaseFragment( @LayoutRes private val layoutId: Int ): Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, sis: Bundle? ): View =
            i.inflate( layoutId, c,false )
}

class TvsFragment: BaseFragment( R.layout.fragment_tvs )
class MoviesFragment: BaseFragment( R.layout.fragment_movies )
class PlaylistsFragment: BaseFragment( R.layout.fragment_playlists )
class EpgsFragment: BaseFragment( R.layout.fragment_epgs )
class SettingsFragment: BaseFragment( R.layout.fragment_settings )
