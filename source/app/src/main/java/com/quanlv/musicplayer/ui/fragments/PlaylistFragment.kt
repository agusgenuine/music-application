package com.quanlv.musicplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.LENGTH_SHORT
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.databinding.FragmentPlaylistBinding
import com.quanlv.musicplayer.extensions.*
import com.quanlv.musicplayer.models.Playlist
import com.quanlv.musicplayer.ui.adapters.PlaylistAdapter
import com.quanlv.musicplayer.ui.fragments.base.BaseFragment
import com.quanlv.musicplayer.ui.viewmodels.PlaylistViewModel
import com.quanlv.musicplayer.utils.BeatConstants.PLAY_LIST_DETAIL
import kotlinx.android.synthetic.main.layout_recyclerview.*
import org.koin.android.ext.android.inject


class PlaylistFragment : BaseFragment<Playlist>() {

    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var binding: FragmentPlaylistBinding

    private val playlistViewModel by inject<PlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateWithBinding(R.layout.fragment_playlist, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        retainInstance = true
    }

    private fun init() {

        playlistAdapter = PlaylistAdapter().apply {
            itemClickListener = this@PlaylistFragment
        }

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && binding.createPlayList.isShown)
                        binding.createPlayList.hide()
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE)
                        binding.createPlayList.show()
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }

        playlistViewModel.playLists().observe(this) {
            playlistAdapter.updateDataSet(it)
        }

        binding.let {
            it.viewModel = playlistViewModel
            it.lifecycleOwner = this
            it.executePendingBindings()

            it.createPlayList.setOnClickListener { createPlaylistDialog() }
        }
    }


    override fun onItemClick(view: View, position: Int, item: Playlist) {
        activity!!.addFragment(
            R.id.nav_host_fragment,
            PlaylistDetailFragment(),
            PLAY_LIST_DETAIL,
            extras = bundleOf(PLAY_LIST_DETAIL to item.id)
        )
    }

    override fun onPopupMenuClick(
        view: View,
        position: Int,
        item: Playlist,
        itemList: List<Playlist>
    ) {
        val deleted = playlistViewModel.delete(item.id)
        if (deleted != -1)
            mainViewModel.binding.mainContainer.snackbar(
                SUCCESS,
                getString(R.string.playlist_deleted_success, item.name),
                LENGTH_SHORT
            )
        else
            mainViewModel.binding.mainContainer.snackbar(
                ERROR,
                getString(R.string.playlist_deleted_error, item.name),
                LENGTH_SHORT,
                action = getString(R.string.retry),
                clickListener = View.OnClickListener {
                    onPopupMenuClick(view, position, item, itemList)
                })
    }
}
