package com.playtogether_android.app.presentation.ui.thunder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.playtogether_android.app.R
import com.playtogether_android.app.databinding.FragmentTabApplyBinding
import com.playtogether_android.app.databinding.FragmentTabLikeBinding
import com.playtogether_android.app.presentation.base.BaseFragment
import com.playtogether_android.app.presentation.ui.thunder.viewmodel.ThunderViewModel
import com.playtogether_android.domain.model.thunder.ThunderTabListData
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabLikeFragment : BaseFragment<FragmentTabLikeBinding>(R.layout.fragment_tab_like) {

    private lateinit var thunderListAdapter: ThunderListAdapter

    private val thunderViewModel: ThunderViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initThunderListAdapter()
        getLikeList()
        observeLikeList()
    }

    private fun initThunderListAdapter() {
        thunderListAdapter = ThunderListAdapter()
        with(binding.rvLikeThunderList) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = thunderListAdapter
        }
    }

    private fun getLikeList() {
        thunderViewModel.getLikeList()
    }

    private fun observeLikeList() {
        thunderViewModel.thundertabListData.observe(viewLifecycleOwner) {
            val thunderTabListData = mutableListOf<ThunderTabListData.Data>()
            thunderTabListData.addAll(it.data)
            thunderListAdapter.thunderList = thunderTabListData

            Log.d("connect-test", it.toString())
        }
    }




}