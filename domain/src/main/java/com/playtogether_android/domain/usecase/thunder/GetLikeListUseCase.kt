package com.playtogether_android.domain.usecase.thunder

import com.playtogether_android.domain.model.thunder.ThunderTabListData
import com.playtogether_android.domain.repository.thunder.ThunderRepository

class GetLikeListUseCase(private val repository: ThunderRepository) {

    suspend operator fun invoke() : ThunderTabListData {
        return repository.getLikeList()
    }
}