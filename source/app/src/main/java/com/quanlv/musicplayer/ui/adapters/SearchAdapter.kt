package com.quanlv.musicplayer.ui.adapters

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.databinding.SearchItemBinding
import com.quanlv.musicplayer.extensions.deepEquals
import com.quanlv.musicplayer.extensions.inflateWithBinding
import com.quanlv.musicplayer.interfaces.ItemClickListener
import com.quanlv.musicplayer.models.*
import com.quanlv.musicplayer.ui.viewmodels.SearchViewModel
import com.quanlv.musicplayer.ui.viewmodels.SongDetailViewModel
import com.quanlv.musicplayer.utils.BeatConstants.ALBUM_TYPE
import com.quanlv.musicplayer.utils.BeatConstants.ARTIST_TYPE
import com.quanlv.musicplayer.utils.BeatConstants.SONG_TYPE

@Suppress("UNCHECKED_CAST")
class SearchAdapter(
    activity: Activity,
    private val viewModel: SearchViewModel,
    itemClickListener: ItemClickListener<MediaItem>,
    private val sc: Int = 2
) : RecyclerView.Adapter<SearchAdapter.ViewHolderSearch>() {

    val songAdapter: SongAdapter = SongAdapter().apply {
        this.itemClickListener = itemClickListener as ItemClickListener<Song>
        showHeader = false
    }
    private val albumAdapter = AlbumAdapter(activity).apply {
        this.itemClickListener = itemClickListener as ItemClickListener<Album>
        spanCount = sc
    }
    private val artistAdapter = ArtistAdapter(activity).apply {
        this.itemClickListener = itemClickListener as ItemClickListener<Artist>
        spanCount = sc
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSearch {
        val viewBinding = parent.inflateWithBinding<SearchItemBinding>(R.layout.search_item)
        return ViewHolderSearch(viewBinding)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ViewHolderSearch, position: Int) {
        holder.bind()
    }

    fun updateDataSet(data: SearchData) {
        val first =
            artistAdapter.artistList.isEmpty() && songAdapter.songList.isEmpty() && albumAdapter.albumList.isEmpty()
        if (!artistAdapter.artistList.deepEquals(data.artistList)) {
            artistAdapter.updateDataSet(data.artistList)
            if (!first) notifyItemChanged(2)
        }
        if (!albumAdapter.albumList.deepEquals(data.albumList)) {
            albumAdapter.updateDataSet(data.albumList)
            if (!first) notifyItemChanged(1)
        }
        if (!songAdapter.songList.deepEquals(data.songList)) {
            songAdapter.updateDataSet(data.songList)
            if (!first) notifyItemChanged(0)
        }
        if (first) notifyDataSetChanged()
    }

    inner class ViewHolderSearch(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.let {
                it.viewModel = viewModel
                it.type = SONG_TYPE
                it.executePendingBindings()
            }
            when (adapterPosition) {
                0 -> {
                    binding.apply {
                        type = SONG_TYPE
                        list.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = songAdapter
                            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                        }
                    }
                }
                1 -> {
                    binding.apply {
                        type = ALBUM_TYPE
                        list.apply {
                            layoutManager = GridLayoutManager(context, sc)
                            adapter = albumAdapter
                        }
                    }
                }
                2 -> {
                    binding.apply {
                        type = ARTIST_TYPE
                        list.apply {
                            layoutManager = GridLayoutManager(context, sc)
                            adapter = artistAdapter
                        }
                    }
                }
            }
        }
    }
}