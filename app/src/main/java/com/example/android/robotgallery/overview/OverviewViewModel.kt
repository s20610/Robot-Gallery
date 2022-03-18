/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.robotgallery.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.robotgallery.network.RobotApi
import com.example.android.robotgallery.network.Robot
import kotlinx.coroutines.launch

enum class RobotApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<RobotApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<RobotApiStatus> = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of MarsPhoto
    // with new values
    private val _photos = MutableLiveData<List<Robot>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val photos: LiveData<List<Robot>> = _photos

    init {
        getRobots()
    }

    private fun getRobots() {

        viewModelScope.launch {
            _status.value = RobotApiStatus.LOADING
            try {
                _photos.value = RobotApi.retrofitService.getRobots()
                _status.value = RobotApiStatus.DONE
            } catch (e: Exception) {
                _status.value = RobotApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }
}
