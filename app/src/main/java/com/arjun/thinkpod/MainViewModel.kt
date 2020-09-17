package com.arjun.thinkpod

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.thinkpod.util.Resource
import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val parser: Parser) : ViewModel() {

    private val _rssChannel = MutableLiveData<Resource<Channel>>()
    val rssChannel: LiveData<Resource<Channel>>
        get() = _rssChannel


    fun fetchFeed(url: String) {
        viewModelScope.launch {
            _rssChannel.postValue(Resource.Loading())
            try {
                val channel = parser.getChannel(url)
                _rssChannel.postValue(Resource.Success(channel))
            } catch (e: Exception) {
                e.printStackTrace()
                _rssChannel.postValue(Resource.Error(e.localizedMessage ?: "Something went wrong"))
            }
        }
    }
}