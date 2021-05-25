package com.app.spacexfan.data.model.rockets

data class RocketsModelItem(
    val id: String,
    val name: String,
    val description: String,
    val flickr_images: List<String>,
    var isFavorite: Boolean
)