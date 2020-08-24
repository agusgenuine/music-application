package com.quanlv.musicplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.alertdialog.actions.AlertItemAction
import com.quanlv.musicplayer.alertdialog.enums.AlertItemTheme
import com.quanlv.musicplayer.databinding.FragmentAlbumBinding
import com.quanlv.musicplayer.extensions.*
import com.quanlv.musicplayer.models.Album
import com.quanlv.musicplayer.ui.adapters.AlbumAdapter
import com.quanlv.musicplayer.ui.fragments.base.BaseFragment
import com.quanlv.musicplayer.ui.viewmodels.AlbumViewModel
import com.quanlv.musicplayer.utils.BeatConstants
import com.quanlv.musicplayer.utils.BeatConstants.ALBUM_KEY
import com.quanlv.musicplayer.utils.GeneralUtils
import com.quanlv.musicplayer.utils.GeneralUtils.PORTRAIT
import com.quanlv.musicplayer.utils.SortModes
import org.koin.android.ext.android.inject

class OnlineFragment : BaseFragment<Album>() {

    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var binding: FragmentAlbumBinding
    private val albumViewModel by inject<AlbumViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateWithBinding(R.layout.fragment_album, container)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        retainInstance = true
    }

    private fun init() {
        val sc = if (GeneralUtils.getOrientation(safeActivity) == PORTRAIT) 2 else 5

        albumAdapter = AlbumAdapter(context).apply {
            showHeader = true
            itemClickListener = this@OnlineFragment
            spanCount = sc
        }

        binding.list.apply {
            layoutManager = GridLayoutManager(context, sc).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == 0) sc else 1
                    }
                }
            }
            adapter = albumAdapter
        }

        albumViewModel.getAlbums()
            .filter { !albumAdapter.albumList.deepEquals(it) }
            .observe(this) { list ->
                albumAdapter.updateDataSet(list)
            }

        binding.let {
            it.viewModel = albumViewModel
            it.lifecycleOwner = this
            it.executePendingBindings()
        }

        createDialog()
    }

    private fun createDialog() {
        dialog = buildDialog(
            getString(R.string.sort_title),
            getString(R.string.sort_msg),
            listOf(
                AlertItemAction(
                    context!!.getString(R.string.sort_az),
                    settingsUtility.albumSortOrder == SortModes.AlbumModes.ALBUM_A_Z,
                    AlertItemTheme.DEFAULT
                ) {
                    it.selected = true
                    settingsUtility.albumSortOrder = SortModes.AlbumModes.ALBUM_A_Z
                    reloadAdapter()
                },
                AlertItemAction(
                    context!!.getString(R.string.sort_za),
                    settingsUtility.albumSortOrder == SortModes.AlbumModes.ALBUM_Z_A,
                    AlertItemTheme.DEFAULT
                ) {
                    it.selected = true
                    settingsUtility.albumSortOrder = SortModes.AlbumModes.ALBUM_Z_A
                    reloadAdapter()
                },
                AlertItemAction(
                    context!!.getString(R.string.sort_year),
                    settingsUtility.albumSortOrder == SortModes.AlbumModes.ALBUM_YEAR,
                    AlertItemTheme.DEFAULT
                ) {
                    it.selected = true
                    settingsUtility.albumSortOrder =
                        SortModes.AlbumModes.ALBUM_YEAR
                    reloadAdapter()
                },
                AlertItemAction(
                    context!!.getString(R.string.artist),
                    settingsUtility.albumSortOrder == SortModes.AlbumModes.ALBUM_ARTIST,
                    AlertItemTheme.DEFAULT
                ) {
                    it.selected = true
                    settingsUtility.albumSortOrder = SortModes.AlbumModes.ALBUM_ARTIST
                    reloadAdapter()
                },
                AlertItemAction(
                    context!!.getString(R.string.song_count),
                    settingsUtility.albumSortOrder == SortModes.AlbumModes.ALBUM_SONG_COUNT,
                    AlertItemTheme.DEFAULT
                ) {
                    it.selected = true
                    settingsUtility.albumSortOrder = SortModes.AlbumModes.ALBUM_SONG_COUNT
                    reloadAdapter()
                }
            )
        )
    }

    private fun reloadAdapter() {
        albumViewModel.update()
    }

    override fun onItemClick(view: View, position: Int, item: Album) {
        activity!!.addFragment(
            R.id.nav_host_fragment,
            AlbumDetailFragment(),
            BeatConstants.ALBUM_DETAIL,
            true,
            bundleOf(ALBUM_KEY to item.id)
        )
    }

    override fun onPopupMenuClick(view: View, position: Int, item: Album, itemList: List<Album>) {
        Toast.makeText(context, "Menu of " + item.title, Toast.LENGTH_SHORT).show()
    }

    override fun onPlayAllClick(view: View) {
        Toast.makeText(context, "Shuffle", Toast.LENGTH_SHORT).show()
    }

    override fun onSortClick(view: View) {
        dialog.show(activity as AppCompatActivity)
    }
}
