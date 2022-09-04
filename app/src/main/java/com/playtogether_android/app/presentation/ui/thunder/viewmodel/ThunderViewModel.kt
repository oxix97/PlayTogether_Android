package com.playtogether_android.app.presentation.ui.thunder.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playtogether_android.domain.model.light.CategoryData
import com.playtogether_android.domain.model.thunder.ThunderTabListData
import com.playtogether_android.domain.usecase.thunder.GetApplyListUseCase
import com.playtogether_android.domain.usecase.thunder.GetLikeListUseCase
import com.playtogether_android.domain.usecase.thunder.GetOpenListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ThunderViewModel @Inject constructor(
    val getApplyListUseCase: GetApplyListUseCase,
    val getOpenListUseCase: GetOpenListUseCase,
    val getLikeListUseCase: GetLikeListUseCase
) : ViewModel() {

    private val _thunderTabListData = MutableLiveData<ThunderTabListData>()
    val thundertabListData: LiveData<ThunderTabListData>
        get() = _thunderTabListData

    private val _thunderApplyList = MutableLiveData<List<CategoryData>>()
    val thunderApplyList: LiveData<List<CategoryData>> = _thunderApplyList

    private val _thunderOpenList = MutableLiveData<List<CategoryData>>()
    val thunderOpenList: LiveData<List<CategoryData>> = _thunderOpenList

    private val _thunderLikeList = MutableLiveData<List<CategoryData>>()
    val thunderLikeList: LiveData<List<CategoryData>> = _thunderLikeList


    //번개탭-신청한 번개 리스트
    fun getApplyList() = viewModelScope.launch {
        kotlin.runCatching { getApplyListUseCase() }
            .onSuccess {
                _thunderApplyList.value = it
                Timber.d("thunder apply : $it")
            }
            .onFailure {
                it.printStackTrace()
                Log.d("getApplyList-fail", "fail")
            }
    }

    fun getOpenList() {
        viewModelScope.launch {
            kotlin.runCatching {
                getOpenListUseCase()
            }.onSuccess {
                _thunderOpenList.value = it
                Timber.d("thunder open : $it")
            }.onFailure {

            }
        }
    }

    fun getLikeList() {
        viewModelScope.launch {
            kotlin.runCatching {
                getLikeListUseCase()
            }.onSuccess {
                _thunderLikeList.value = it
                Timber.d("thunder like : $it")
            }.onFailure {

            }
        }
    }

    //번개탭-오픈한 번개 리스트
//    fun getOpenList() = viewModelScope.launch {
//        kotlin.runCatching { getOpenListUseCase() }
//            .onSuccess {
//                _thunderTabListData.postValue(it)
//                Log.d("getOpenList", it.toString())
//            }
//            .onFailure {
//                it.printStackTrace()
//                Log.d("getOpenList-fail", "fail")
//            }
//    }

    //번개탭-찜한 번개 리스트
//    fun getLikeList() = viewModelScope.launch {
//        kotlin.runCatching { getLikeListUseCase() }
//            .onSuccess {
//                _thunderTabListData.postValue(it)
//                Log.d("getLikeList", it.toString())
//            }
//            .onFailure {
//                it.printStackTrace()
//                Log.d("getOpenList-fail", "fail")
//            }
//    }


}