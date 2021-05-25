package com.app.spacexfan.ui.main.home.rocket_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.spacexfan.data.custom.SingleLiveEvent
import com.app.spacexfan.data.model.rockets.RocketsModelItem
import com.app.spacexfan.data.repository.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketDetailViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    val postRocketDetail: SingleLiveEvent<RocketsModelItem> by lazy {
        SingleLiveEvent()
    }

    fun getRocketDetail(id: String) {
        viewModelScope.launch {
            val retrofitPost = repository.getRocketDetail(id)
            if (retrofitPost.data!=null) {
                postRocketDetail.postValue(retrofitPost.data)
                Log.d("rocket_response",retrofitPost.data.toString())
            }
        }
    }

}