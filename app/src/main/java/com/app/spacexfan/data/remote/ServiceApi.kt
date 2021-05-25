package com.app.spacexfan.data.remote

import com.app.spacexfan.data.model.rockets.RocketsModel
import com.app.spacexfan.data.model.rockets.RocketsModelItem
import com.app.spacexfan.data.model.upcoming_launches.UpcomingLaunchesModel
import com.app.spacexfan.data.model.upcoming_launches.UpcomingLaunchesModelItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceApi {

    //Get All Rockets
    @GET("rockets")
    suspend fun getRockets(): Response<RocketsModel>

    //Get Rocket Detail
    @GET("rockets/{id}")
    suspend fun getRocketDetail(
        @Path("id") id: String): Response<RocketsModelItem>

    //Get Upcoming Launches
    @GET("launches/upcoming")
    suspend fun getUpcomingLaunches(): Response<UpcomingLaunchesModel>

    //Get Launch Detail
    @GET("launches/{id}")
    suspend fun getLaunchDetail(
        @Path("id") id: String): Response<UpcomingLaunchesModelItem>
}