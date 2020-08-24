package com.quanlv.musicplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.databinding.FragmentLibraryBinding
import com.quanlv.musicplayer.extensions.inflateWithBinding
import com.quanlv.musicplayer.extensions.safeActivity
import com.quanlv.musicplayer.ui.activities.MainActivity
import com.quanlv.musicplayer.ui.adapters.ViewPagerAdapter
import com.quanlv.musicplayer.ui.fragments.base.BaseSongDetailFragment
import com.quanlv.musicplayer.utils.AutoClearBinding


class LibraryFragment : BaseSongDetailFragment() {

    private var binding by AutoClearBinding<FragmentLibraryBinding>(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateWithBinding(R.layout.fragment_library, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val didPermissionsGrant = (safeActivity as MainActivity).didPermissionsGrant()
        if (didPermissionsGrant) init()
        binding.apply {
            viewModel = mainViewModel
            this.didPermissionsGrant = didPermissionsGrant
            executePendingBindings()

            lifecycleOwner = this@LibraryFragment
        }
    }

    private fun init() {
        binding.apply {
            initViewPager(binding.pagerSortMode)
            tabsContainer.setupWithViewPager(pagerSortMode)
        }
    }

    private fun initViewPager(viewPager: ViewPager) {
        val listSortModeAdapter = ViewPagerAdapter(safeActivity.supportFragmentManager).apply {
            addFragment(OnlineFragment(), getString(R.string.online))
            addFragment(FavoriteFragment(), getString(R.string.favorites))
            addFragment(PlaylistFragment(), getString(R.string.playlists))
            addFragment(SongFragment(), getString(R.string.songs))
            addFragment(AlbumFragment(), getString(R.string.albums))
            addFragment(ArtistFragment(), getString(R.string.artists))
            addFragment(FolderFragment(), getString(R.string.folders))
        }

        viewPager.apply {
            adapter = listSortModeAdapter
            offscreenPageLimit = 1
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(s: Int) = Unit
                override fun onPageScrolled(p: Int, po: Float, pop: Int) = Unit
                override fun onPageSelected(p: Int) {
                    settingsUtility.startPageIndexSelected = p
                }
            })
            setCurrentItem(settingsUtility.startPageIndexSelected, false)
        }
    }
}
