package com.quanlv.musicplayer.ui.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.databinding.FragmentSearchBinding
import com.quanlv.musicplayer.extensions.*
import com.quanlv.musicplayer.models.Album
import com.quanlv.musicplayer.models.Artist
import com.quanlv.musicplayer.models.MediaItem
import com.quanlv.musicplayer.models.Song
import com.quanlv.musicplayer.ui.adapters.SearchAdapter
import com.quanlv.musicplayer.ui.fragments.base.BaseFragment
import com.quanlv.musicplayer.ui.viewmodels.PlaylistViewModel
import com.quanlv.musicplayer.ui.viewmodels.SearchViewModel
import com.quanlv.musicplayer.utils.BeatConstants.ALBUM_DETAIL
import com.quanlv.musicplayer.utils.BeatConstants.ALBUM_KEY
import com.quanlv.musicplayer.utils.BeatConstants.ARTIST_DETAIL
import com.quanlv.musicplayer.utils.BeatConstants.ARTIST_KEY
import com.quanlv.musicplayer.utils.BeatConstants.PLAY_LIST_TYPE
import com.quanlv.musicplayer.utils.GeneralUtils.PORTRAIT
import com.quanlv.musicplayer.utils.GeneralUtils.getExtraBundle
import com.quanlv.musicplayer.utils.GeneralUtils.getOrientation
import com.quanlv.musicplayer.utils.GeneralUtils.toggleShowKeyBoard
import org.koin.android.ext.android.inject

class SearchFragment : BaseFragment<MediaItem>(), TextWatcher {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var searchAdapter: SearchAdapter

    private val searchViewModel by inject<SearchViewModel>()
    private val playlistViewModel by inject<PlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateWithBinding(R.layout.fragment_search, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        retainInstance = true
    }

    private fun init() {
        val sc = if (getOrientation(safeActivity) == PORTRAIT) 2 else 5

        searchAdapter = SearchAdapter(safeActivity, searchViewModel, this, sc)

        binding.apply {
            searchSrcText.apply {
                addTextChangedListener(this@SearchFragment)
                toggleShowKeyBoard(context, this, true)
            }

            back.setOnClickListener {
                toggleShowKeyBoard(safeActivity, searchSrcText, false)
                activity!!.onBackPressed()
            }

            binding.searchList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = searchAdapter
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }

            close.setOnClickListener { searchSrcText.text.clear() }
        }

        searchViewModel.searchLiveData().observe(this) {
            searchAdapter.updateDataSet(it)
        }

        binding.let {
            it.viewModel = searchViewModel
            it.lifecycleOwner = this
            it.status = false
            it.executePendingBindings()
        }
    }

    override fun onItemClick(view: View, position: Int, item: MediaItem) {
        super.onItemClick(view, position, item)
        when (item) {
            is Song -> songClicked(item)
            is Album -> albumClicked(item)
            is Artist -> artistClicked(item)
        }
        toggleShowKeyBoard(safeActivity, binding.searchSrcText, false)
    }

    override fun onPopupMenuClick(
        view: View,
        position: Int,
        item: MediaItem,
        itemList: List<MediaItem>
    ) {
        super.onPopupMenuClick(view, position, item, itemList)
        if (item is Song) songPopup(item, view)
    }

    private fun songPopup(song: Song, view: View) {
        powerMenu!!.showAsAnchorRightTop(view)
        playlistViewModel.playLists().observe(this) {
            buildPlaylistMenu(it, song)
        }
    }

    private fun songClicked(item: Song) {
        val extras = getExtraBundle(longArrayOf(item.id), PLAY_LIST_TYPE)
        mainViewModel.mediaItemClicked(item.toMediaItem(), extras)
    }

    private fun albumClicked(item: Album) {
        activity!!.addFragment(
            R.id.nav_host_fragment,
            AlbumDetailFragment(),
            ALBUM_DETAIL,
            true,
            bundleOf(ALBUM_KEY to item.id)
        )
    }

    private fun artistClicked(item: Artist) {
        activity!!.addFragment(
            R.id.nav_host_fragment,
            ArtistDetailFragment(),
            ARTIST_DETAIL,
            true,
            bundleOf(ARTIST_KEY to item.id)
        )
    }

    override fun onTextChanged(src: CharSequence?, start: Int, before: Int, count: Int) {
        searchViewModel.search(src.toString())
        binding.status = src?.isNotEmpty()
    }

    override fun afterTextChanged(s: Editable?) = Unit
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
}
