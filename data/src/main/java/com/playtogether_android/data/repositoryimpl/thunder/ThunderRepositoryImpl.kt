package com.playtogether_android.data.repositoryimpl.thunder

import com.playtogether_android.data.datasource.thunder.ThunderDataSource
import com.playtogether_android.data.mapper.thunder.ThunderMapper
import com.playtogether_android.domain.model.thunder.ThunderTabListData
import com.playtogether_android.domain.repository.thunder.ThunderRepository

class ThunderRepositoryImpl(private val thunderDataSource: ThunderDataSource) : ThunderRepository {

    //번개탭-신청한 번개 리스트
    override suspend fun getApplyList(): ThunderTabListData {
        return ThunderMapper.mapperToThunderTabListData(thunderDataSource.getApplyList())
    }

    override suspend fun getOpenList(): ThunderTabListData {
        return ThunderMapper.mapperToThunderTabListData(thunderDataSource.getOpenList())
    }

    override suspend fun getLikeList(): ThunderTabListData {
        return ThunderMapper.mapperToThunderTabListData(thunderDataSource.getLikeList())
    }

}