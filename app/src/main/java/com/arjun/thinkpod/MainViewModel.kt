package com.arjun.thinkpod

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.thinkpod.network.ITunesApi
import com.arjun.thinkpod.network.response.ITunesResponse
import com.arjun.thinkpod.util.Resource
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val iTunesApi: ITunesApi) : ViewModel() {

    private val _rssITunesResponse = MutableLiveData<Resource<ITunesResponse>>()
    val rssITunesResponse: LiveData<Resource<ITunesResponse>>
        get() = _rssITunesResponse


    fun fetchTopPodcast() {
        viewModelScope.launch {
            _rssITunesResponse.postValue(Resource.Loading())
            try {
                val channel = iTunesApi.getTopPodcasts(COUNTRY)
                _rssITunesResponse.postValue(Resource.Success(channel))
            } catch (e: Exception) {
                e.printStackTrace()
                _rssITunesResponse.postValue(
                    Resource.Error(
                        e.localizedMessage ?: "Something went wrong"
                    )
                )
            }
        }
    }

    companion object {
        const val COUNTRY = "us"
    }
}