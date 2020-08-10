package com.quanlv.musicplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.databinding.FragmentArtistDetailBinding
import com.quanlv.musicplayer.extensions.*
import com.quanlv.musicplayer.interfaces.ItemClickListener
import com.quanlv.musicplayer.models.Album
import com.quanlv.musicplayer.models.Artist
import com.quanlv.musicplayer.models.MediaItem
import com.quanlv.musicplayer.models.Song
import com.quanlv.musicplayer.ui.adapters.AlbumAdapter
import com.quanlv.musicplayer.ui.fragments.base.BaseFragment
import com.quanlv.musicplayer.ui.viewmodels.ArtistViewModel
import com.quanlv.musicplayer.ui.viewmodels.FavoriteViewModel
import com.quanlv.musicplayer.ui.viewmodels.PlaylistViewModel
import com.quanlv.musicplayer.utils.BeatConstants
import kotlinx.android.synthetic.main.layout_recyclerview.*
import org.koin.android.ext.android.inject

class ArtistDetailFragment : BaseFragment<MediaItem>() {

    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var binding: FragmentArtistDetailBinding
    private lateinit var artist: Artist
    private val artistViewModel by inject<ArtistViewModel>()
    private val playlistViewModel by inject<PlaylistViewModel>()
    private val favoriteViewModel by inject<FavoriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateWithBinding(R.layout.fragment_artist_detail, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        retainInstance = true
    }

    @Suppress("UNCHECKED_CAST")
    private fun init() {
        val id = arguments!!.getLong(BeatConstants.ARTIST_KEY)
        artist = artistViewModel.getArtist(id)

        albumAdapter = AlbumAdapter(context).apply {
            itemClickListener = this@ArtistDetailFragment as ItemClickListener<Album>
            artistDetail = true
        }

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = albumAdapter
            clipToOutline = true
        }

        binding.addFavorites.setOnClickListener { toggleAddFav() }

        artistViewModel.getArtistAlbums(artist.id)
            .filter { !albumAdapter.albumList.deepEquals(it) }
            .observe(this) {
                albumAdapter.updateDataSet(it)
            }

        binding.let {
            it.artist = artist
            it.viewModel = mainViewModel
            it.executePendingBindings()

            it.lifecycleOwner = this
        }
    }

    private fun albumClicked(item: Album) {
        activity!!.addFragment(
            R.id.nav_host_fragment,
            AlbumDetailFragment(),
            BeatConstants.ALBUM_DETAIL,
            true,
            bundleOf(BeatConstants.ALBUM_KEY to item.id)
        )
    }

    override fun onItemClick(view: View, position: Int, item: MediaItem) {
        when (item) {
            is Song -> Toast.makeText(context, "Song: ${item.title}", Toast.LENGTH_SHORT).show()
            is Album -> albumClicked(item)
        }
    }

    override fun onPopupMenuClick(
        view: View,
        position: Int,
        item: MediaItem,
        itemList: List<MediaItem>
    ) {
        item as Song
        powerMenu!!.showAsAnchorRightTop(view)
        playlistViewModel.playLists().observe(this) {
            buildPlaylistMenu(it, item)
        }
    }

    private fun toggleAddFav() {
        if (favoriteViewModel.favExist(artist.id)) {
            val resp = favoriteViewModel.deleteFavorites(longArrayOf(artist.id))
            showSnackBar(view, resp, R.string.artist_no_fav_ok)
        } else {
            val resp = favoriteViewModel.create(artist.toFavorite())
            showSnackBar(view, resp, R.string.artist_fav_ok)
        }
    }
}
