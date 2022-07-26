package com.playtogether_android.data.mapper.onboarding


import com.playtogether_android.data.model.request.onboarding.RequestMakeCrew
import com.playtogether_android.data.model.request.onboarding.RequestRegisterCrew
import com.playtogether_android.data.model.response.onboarding.ResponseGetList
import com.playtogether_android.data.model.response.onboarding.ResponseMakeCrew
import com.playtogether_android.data.model.response.onboarding.ResponseRegisterCrew
import com.playtogether_android.domain.model.onboarding.*

object OnBoardingMapper {
    //동아리 참여 : Response
    fun mapperToRegisterCrewData(responseRegisterCrew: ResponseRegisterCrew) : RegisterCrewData {
        return RegisterCrewData(
            success = responseRegisterCrew.success,
            crewName = responseRegisterCrew.data.crewName
        )
    }

    //동아리 참여 : Request
    fun mapperToRegisterCrewItem(registerCrewItem: RegisterCrewItem) : RequestRegisterCrew {
        return RequestRegisterCrew(
            crewCode = registerCrewItem.crewCode
        )
    }


    //동아리 개설 response
    fun mapperToMakeCrewData(responseMakeCrew: ResponseMakeCrew) : MakeCrewData {
        return MakeCrewData(
            success = responseMakeCrew.success,
            code = responseMakeCrew.data.code,
            id = responseMakeCrew.data.id,
            name = responseMakeCrew.data.name
        )
    }

    //동아리 개설 request
    fun mapperToMakeCrewItem(makeCrewItem: MakeCrewItem) : RequestMakeCrew{
        return RequestMakeCrew(
            crewName = makeCrewItem.crewName,
            description = makeCrewItem.description
        )
    }

    //동아리 리스트 조회 response
    fun mapperToGetList(responseGetList: ResponseGetList) :CrewListData{
        return CrewListData(
            data = CrewListData.Data(
                crewList = responseGetList.data.crewList.map {
                    CrewListData.Data.CrewList(
                        id = it.id,
                        name = it.name
                    )
                }
            ),
            success = responseGetList.success
        )
    }

}