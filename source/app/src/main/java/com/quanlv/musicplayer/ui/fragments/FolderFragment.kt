package com.quanlv.musicplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.databinding.FragmentFolderBinding
import com.quanlv.musicplayer.extensions.*
import com.quanlv.musicplayer.models.Folder
import com.quanlv.musicplayer.ui.adapters.FolderAdapter
import com.quanlv.musicplayer.ui.fragments.base.BaseFragment
import com.quanlv.musicplayer.ui.viewmodels.FolderViewModel
import com.quanlv.musicplayer.utils.BeatConstants.FOLDER_KEY
import kotlinx.android.synthetic.main.layout_recyclerview.*
import org.koin.android.ext.android.inject

class FolderFragment : BaseFragment<Folder>() {

    private val viewModel by inject<FolderViewModel>()
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var binding: FragmentFolderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateWithBinding(R.layout.fragment_folder, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        retainInstance = true
    }

    private fun init() {
        folderAdapter = FolderAdapter(context).apply {
            itemClickListener = this@FolderFragment
        }

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = folderAdapter
        }

        viewModel.getFolders()
            .filter { !folderAdapter.folderList.deepEquals(it) }
            .observe(this) {
            folderAdapter.updateDataSet(it)
        }

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
            it.executePendingBindings()
        }
    }

    override fun onItemClick(view: View, position: Int, item: Folder) {
        activity!!.addFragment(
            R.id.nav_host_fragment,
            FolderDetailFragment(),
            FOLDER_KEY,
            true,
            bundleOf(FOLDER_KEY to item.id)
        )
    }
}
