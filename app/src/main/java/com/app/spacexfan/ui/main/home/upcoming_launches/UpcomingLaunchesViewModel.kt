package com.app.spacexfan.ui.main.home.upcoming_launches

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.spacexfan.data.model.upcoming_launches.UpcomingLaunchesModel
import com.app.spacexfan.data.repository.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingLaunchesViewModel @Inject constructor(
    private val repository: SpaceXRepository) : ViewModel() {

    val postUpcomingLaunches: MutableLiveData<UpcomingLaunchesModel> by lazy {
        MutableLiveData<UpcomingLaunchesModel>()
    }

    fun getUpcomingLaunches() {
        viewModelScope.launch {
            val retrofitPost = repository.getUpcomingLaunches()
            if (retrofitPost.data!=null) {
                postUpcomingLaunches.postValue(retrofitPost.data)
                Log.d("launches_response",retrofitPost.data.toString())
            }
        }
    }
}