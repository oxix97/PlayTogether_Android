package com.playtogether_android.app.presentation.ui.thunder.list.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.playtogether_android.app.R
import com.playtogether_android.app.databinding.FragmentThunderGoBinding
import com.playtogether_android.app.presentation.base.BaseFragment
import com.playtogether_android.app.presentation.ui.thunder.list.adapter.ThunderCategoryListItemAdapter
import com.playtogether_android.app.presentation.ui.thunder.list.viewmodel.ThunderListViewModel
import com.playtogether_android.app.presentation.ui.thunder.list.viewmodel.ThunderListViewModel.Companion.CATEGORY_GO
import com.playtogether_android.app.presentation.ui.thunder.list.viewmodel.ThunderListViewModel.Companion.MORE
import com.playtogether_android.app.util.SpaceItemDecoration

class ThunderGoFragment :
    BaseFragment<FragmentThunderGoBinding>(R.layout.fragment_thunder_go) {
    private lateinit var listAdapter: ThunderCategoryListItemAdapter
    private val thunderListViewModel: ThunderListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listViewModel = thunderListViewModel
        binding.lifecycleOwner = this
        initAdapter()
        observingList()
        observingScrap()
    }

    private fun initAdapter() {
        listAdapter = ThunderCategoryListItemAdapter { thunderId, position ->
            adapterLamda(thunderId, position)
        }
        with(binding.rvThundergoContainer) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpaceItemDecoration(0, 10, 0, 0))
            adapter = listAdapter
            itemAnimator = null
            addOnScrollListener(MyScrollListener())
        }
    }

    private fun adapterLamda(thunderId: Int, position: Int) {
        (activity as ThunderListActivity).clickThunderListItem(thunderId)
        thunderListViewModel.adapterPosition = position
    }

    inner class MyScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!binding.rvThundergoContainer.canScrollVertically(1)) {
                if (thunderListViewModel.isLastPage) return
                thunderListViewModel.getLightCategoryList(MORE, CATEGORY_GO)
            }
        }
    }

    private fun observingList() {
        thunderListViewModel.goingItemList.observe(viewLifecycleOwner) {
            listAdapter.initList(it)
        }
    }

    private fun changeScrapCount() {
        with(thunderListViewModel) {
            if ((prevScrapState ?: return) and !((laterScrapState.value) ?: return)) {
                listAdapter.updateScrapCount(
                    adapterPosition ?: return,
                    ThunderListViewModel.SCRAP_MINUS
                )
            } else if (!(prevScrapState ?: return) and (laterScrapState.value ?: return)) {
                listAdapter.updateScrapCount(
                    adapterPosition ?: return,
                    ThunderListViewModel.SCRAP_PLUS
                )
            }
        }
    }

    private fun observingScrap() {
        thunderListViewModel.laterScrapState.observe(viewLifecycleOwner) {
            changeScrapCount()
        }
    }
}
