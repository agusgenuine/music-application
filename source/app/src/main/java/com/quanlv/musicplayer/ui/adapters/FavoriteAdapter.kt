package com.quanlv.musicplayer.ui.adapters

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.alertdialog.utils.ViewUtils.dip2px
import com.quanlv.musicplayer.databinding.FavoriteItemBinding
import com.quanlv.musicplayer.extensions.inflateWithBinding
import com.quanlv.musicplayer.interfaces.ItemClickListener
import com.quanlv.musicplayer.models.Favorite
import com.quanlv.musicplayer.utils.GeneralUtils.screenWidth

class FavoriteAdapter(private val context: Context?) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolderFavorite>() {

    private var lastClick = 0L
    var favoriteList: MutableList<Favorite> = mutableListOf()
    var itemClickListener: ItemClickListener<Favorite>? = null
    var spanCount: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.ViewHolderFavorite {
        val viewBinding = parent.inflateWithBinding<FavoriteItemBinding>(R.layout.favorite_item)
        return ViewHolderFavorite(viewBinding)
    }

    override fun getItemCount() = favoriteList.size

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolderFavorite, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateDataSet(newList: List<Favorite>) {
        favoriteList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Favorite {
        return favoriteList[position]
    }

    inner class ViewHolderFavorite(private val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(artist: Favorite) {
            binding.apply {
                this.favorite = artist
                executePendingBindings()

                showDetails.setOnClickListener(this@ViewHolderFavorite)
                container.layoutParams.apply {
                    height = screenWidth / spanCount + dip2px(context!!, 22)
                    width = screenWidth / spanCount - dip2px(context, 6)
                }
            }
        }

        override fun onClick(view: View) {
            if (SystemClock.elapsedRealtime() - lastClick < 300) {
                return
            }
            lastClick = SystemClock.elapsedRealtime()
            if (itemClickListener != null) itemClickListener!!.onItemClick(
                view,
                adapterPosition,
                getItem(adapterPosition)
            )
        }
    }
}