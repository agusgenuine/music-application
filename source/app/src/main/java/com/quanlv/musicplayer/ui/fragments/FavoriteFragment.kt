package com.quanlv.musicplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.databinding.FragmentFavoriteBinding
import com.quanlv.musicplayer.extensions.*
import com.quanlv.musicplayer.models.Favorite
import com.quanlv.musicplayer.ui.adapters.FavoriteAdapter
import com.quanlv.musicplayer.ui.fragments.base.BaseFragment
import com.quanlv.musicplayer.ui.viewmodels.FavoriteViewModel
import com.quanlv.musicplayer.utils.BeatConstants
import com.quanlv.musicplayer.utils.BeatConstants.ALBUM_DETAIL
import com.quanlv.musicplayer.utils.BeatConstants.ALBUM_KEY
import com.quanlv.musicplayer.utils.BeatConstants.ALBUM_TYPE
import com.quanlv.musicplayer.utils.BeatConstants.ARTIST_DETAIL
import com.quanlv.musicplayer.utils.BeatConstants.ARTIST_TYPE
import com.quanlv.musicplayer.utils.BeatConstants.FAVORITE_KEY
import com.quanlv.musicplayer.utils.BeatConstants.FOLDER_KEY
import com.quanlv.musicplayer.utils.BeatConstants.FOLDER_TYPE
import com.quanlv.musicplayer.utils.GeneralUtils
import org.koin.android.ext.android.inject

class FavoriteFragment : BaseFragment<Favorite>() {

    private val viewModel by inject<FavoriteViewModel>()
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateWithBinding(R.layout.fragment_favorite, container)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        retainInstance = true
    }

    private fun init() {
        val sc = if (GeneralUtils.getOrientation(safeActivity) == GeneralUtils.PORTRAIT) 2 else 5

        favoriteAdapter = FavoriteAdapter(context).apply {
            itemClickListener = this@FavoriteFragment
            spanCount = sc
        }

        viewModel.getFavorites()
            .filter { !favoriteAdapter.favoriteList.deepEquals(it) }
            .observe(this) {
            favoriteAdapter.updateDataSet(it)
        }

        binding.list.apply {
            layoutManager = GridLayoutManager(context, sc)
            adapter = favoriteAdapter
        }

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
            it.executePendingBindings()
        }
    }

    override fun onItemClick(view: View, position: Int, item: Favorite) {
        when (item.type) {
            ARTIST_TYPE -> {

                activity!!.addFragment(
                    R.id.nav_host_fragment,
                    ArtistDetailFragment(),
                    ARTIST_DETAIL,
                    true,
                    bundleOf(BeatConstants.ARTIST_KEY to item.id)
                )
            }
            ALBUM_TYPE -> {
                activity!!.addFragment(
                    R.id.nav_host_fragment,
                    AlbumDetailFragment(),
                    ALBUM_DETAIL,
                    true,
                    bundleOf(ALBUM_KEY to item.id)
                )
            }
            FOLDER_TYPE -> {
                activity!!.addFragment(
                    R.id.nav_host_fragment,
                    FolderDetailFragment(),
                    FOLDER_KEY,
                    true,
                    bundleOf(FOLDER_KEY to item.id)
                )
            }
            else -> {
                activity!!.addFragment(
                    R.id.nav_host_fragment,
                    FavoriteDetailFragment(),
                    FAVORITE_KEY,
                    true,
                    bundleOf(FAVORITE_KEY to item.id)
                )
            }
        }
    }
}
