package com.app.spacexfan.ui.main.home.rockets

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.spacexfan.data.model.rockets.RocketsModel
import com.app.spacexfan.data.repository.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketsViewModel @Inject constructor(
    private val repository: SpaceXRepository) : ViewModel() {

    val postRockets: MutableLiveData<RocketsModel> by lazy {
        MutableLiveData<RocketsModel>()
    }

    fun getRockets() {
        viewModelScope.launch {
            val retrofitPost = repository.getRockets()
            if (retrofitPost.data!=null) {
                postRockets.postValue(retrofitPost.data)
                Log.d("rockets_response",retrofitPost.data.toString())
            }
        }
    }
}