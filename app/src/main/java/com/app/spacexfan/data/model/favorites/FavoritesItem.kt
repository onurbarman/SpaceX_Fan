package com.app.spacexfan.data.model.favorites

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class FavoritesItem {
    constructor()

    constructor(id: String, name: String, image: String) {
        this.id=id
        this.name=name
        this.image=image
    }

    var id: String = ""
    var name: String = ""
    var image: String = ""

}



