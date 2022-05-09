package com.playtogether_android.app.presentation.ui.home.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.playtogether_android.app.R
import com.playtogether_android.app.databinding.FragmentHomeBinding
import com.playtogether_android.app.presentation.base.BaseFragment
import com.playtogether_android.app.presentation.ui.home.adapter.HomeHotAdapter
import com.playtogether_android.app.presentation.ui.home.adapter.HomeNewAdapter
import com.playtogether_android.app.presentation.ui.home.viewmodel.HomeViewModel
import com.playtogether_android.app.util.viewPagerAnimation

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeVieModel: HomeViewModel by activityViewModels()
    private lateinit var hotAdapter: HomeHotAdapter
    private lateinit var newAdapter: HomeNewAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initData() {
//        homeVieModel.tempList
    }

    private fun initView() {
        binding.btnHomeFloat.layoutParams.apply {
            width = resources.getDimension(R.dimen.fab_home_size).toInt()
            height = resources.getDimension(R.dimen.fab_home_size).toInt()
        }
        refreshView()
        initAdapter()
        initBottomDialog()
    }

    private fun initAdapter() {
        hotListAdapter()
        newListAdapter()
    }

    private fun hotListAdapter() {
        hotAdapter = HomeHotAdapter()
        with(binding.vpHomeHotContainer) {
            adapter = hotAdapter
            requireActivity().viewPagerAnimation(binding.vpHomeHotContainer)
        }

        hotAdapter.submitList(homeVieModel.tempList)
    }

    private fun newListAdapter() {
        newAdapter = HomeNewAdapter()
        with(binding.vpHomeNewContainer) {
            adapter = newAdapter
            requireActivity().viewPagerAnimation(binding.vpHomeNewContainer)
        }
        newAdapter.submitList(homeVieModel.tempList)
    }

    private fun refreshView() {
        with(binding) {
//            lsrlHomeContainer.setOnRefreshListener {
//                //해당 부분에 애니메이션 넣는건가? ex) 배경 0.5초 검은색
//                lsrlHomeContainer.isRefreshing = false
//            }
        }
    }

    private fun initBottomDialog() {
        binding.llHomeGroupTitleContainer.setOnClickListener {
            val bottomSheetDialog = HomeFragmentDialog()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "init bottom_sheet")
        }
    }

}
