package com.app.spacexfan.data.repository

import com.app.spacexfan.data.model.rockets.RocketsModel
import com.app.spacexfan.data.model.rockets.RocketsModelItem
import com.app.spacexfan.data.model.upcoming_launches.UpcomingLaunchesModel
import com.app.spacexfan.data.model.upcoming_launches.UpcomingLaunchesModelItem
import com.app.spacexfan.data.remote.Resource
import com.app.spacexfan.data.remote.ServiceClientInstance
import com.app.spacexfan.utils.Utils.safeApiCall

class SpaceXRepository {
    suspend fun getRockets(): Resource<RocketsModel> {
        return safeApiCall(call = { ServiceClientInstance.getInstance().api.getRockets() })
    }

    suspend fun getRocketDetail(id: String): Resource<RocketsModelItem> {
        return safeApiCall(call = { ServiceClientInstance.getInstance().api.getRocketDetail(id) })
    }

    suspend fun getUpcomingLaunches(): Resource<UpcomingLaunchesModel> {
        return safeApiCall(call = { ServiceClientInstance.getInstance().api.getUpcomingLaunches() })
    }

    suspend fun getLaunchDetail(id: String): Resource<UpcomingLaunchesModelItem> {
        return safeApiCall(call = { ServiceClientInstance.getInstance().api.getLaunchDetail(id) })
    }
}