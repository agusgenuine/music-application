package com.quanlv.musicplayer.ui.fragments.base

import android.os.Bundle
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.extensions.safeActivity
import com.quanlv.musicplayer.models.MediaItem
import com.quanlv.musicplayer.ui.fragments.LyricFragment
import com.quanlv.musicplayer.ui.fragments.SongDetailFragment

open class BaseSongDetailFragment : BaseFragment<MediaItem>() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showHideBottomSheet()
    }

    override fun onPause() {
        showHideBottomSheet()
        super.onPause()
    }

    override fun onResume() {
        showHideBottomSheet()
        super.onResume()
    }

    private fun showHideBottomSheet() {
        val currentData = songDetailViewModel.currentData.value ?: return
        if (currentData.id == 0L) return
        val fragment = safeActivity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (fragment is SongDetailFragment || fragment is LyricFragment) {
            mainViewModel.hideMiniPlayer()
        } else {
            mainViewModel.showMiniPlayer()
        }
    }
}