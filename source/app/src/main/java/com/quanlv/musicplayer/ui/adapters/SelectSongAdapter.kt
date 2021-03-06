package com.quanlv.musicplayer.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.databinding.SelectSongItemBinding
import com.quanlv.musicplayer.extensions.inflateWithBinding
import com.quanlv.musicplayer.interfaces.ItemClickListener
import com.quanlv.musicplayer.models.Song

class SelectSongAdapter(
    private val itemClickListener: ItemClickListener<Song>
) : RecyclerView.Adapter<SelectSongAdapter.ViewHolderSong>() {

    var songList: MutableList<Song> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSong {
        val viewBinding =
            parent.inflateWithBinding<SelectSongItemBinding>(R.layout.select_song_item)
        return ViewHolderSong(viewBinding)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun onBindViewHolder(holder: ViewHolderSong, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItem(position: Int): Song {
        return songList[position]
    }

    fun updateDataSet(list: MutableList<Song>) {
        songList = list
        notifyDataSetChanged()
    }

    inner class ViewHolderSong(private val binding: SelectSongItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(song: Song) {
            binding.apply {
                this.song = song
                executePendingBindings()

                container.setOnClickListener(this@ViewHolderSong)
                selected.setOnClickListener(this@ViewHolderSong)
            }
        }

        override fun onClick(view: View) {
            itemClickListener.onItemClick(
                binding.selected,
                adapterPosition,
                getItem(adapterPosition)
            )
        }
    }

}