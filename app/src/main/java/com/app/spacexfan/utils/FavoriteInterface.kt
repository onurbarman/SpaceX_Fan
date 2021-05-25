package com.app.spacexfan.utils

import com.app.spacexfan.data.model.rockets.RocketsModelItem

interface FavoriteInterface {
    fun favoriteClick(rocket: RocketsModelItem)
}