package com.app.spacexfan.ui.main.home.launch_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.spacexfan.data.custom.SingleLiveEvent
import com.app.spacexfan.data.model.upcoming_launches.UpcomingLaunchesModelItem
import com.app.spacexfan.data.repository.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchDetailViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    val postLaunchDetail: SingleLiveEvent<UpcomingLaunchesModelItem> by lazy {
        SingleLiveEvent()
    }

    fun getLaunchDetail(id: String) {
        viewModelScope.launch {
            val retrofitPost = repository.getLaunchDetail(id)
            if (retrofitPost.data!=null) {
                postLaunchDetail.postValue(retrofitPost.data)
                Log.d("launch_response",retrofitPost.data.toString())
            }
        }
    }
}