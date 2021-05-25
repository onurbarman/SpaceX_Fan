package com.app.spacexfan.data.model.upcoming_launches

data class UpcomingLaunchesModelItem(
    val id: String,
    val name: String,
    val date_utc: String,
    val flight_number: Int,
    val links: Links,
)