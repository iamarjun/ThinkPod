package com.arjun.thinkpod

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.thinkpod.model.xml.RssFeed
import com.arjun.thinkpod.network.ITunesApi
import com.arjun.thinkpod.network.response.ITunesResponse
import com.arjun.thinkpod.network.response.SearchResponse
import com.arjun.thinkpod.util.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(private val iTunesApi: ITunesApi) : ViewModel() {

    private val _iTunesResponse = MutableLiveData<Resource<ITunesResponse>>()
    val iTunesResponse: LiveData<Resource<ITunesResponse>>
        get() = _iTunesResponse

    private val _rssFeed = MutableLiveData<Resource<RssFeed>>()
    val rssFeed: LiveData<Resource<RssFeed>>
        get() = _rssFeed

    private val _searchResponse = MutableLiveData<Resource<SearchResponse>>()
    val searchResponse: LiveData<Resource<SearchResponse>>
        get() = _searchResponse

    fun fetchTopPodcast() {
        viewModelScope.launch {
            _iTunesResponse.postValue(Resource.Loading())
            try {
                val channel = iTunesApi.getTopPodcasts(COUNTRY)
                _iTunesResponse.postValue(Resource.Success(channel))
            } catch (e: Exception) {
                e.printStackTrace()
                _iTunesResponse.postValue(
                    Resource.Error(
                        e.localizedMessage ?: "Something went wrong"
                    )
                )
            }
        }
    }

    private fun fetchRssFeed(url: String) {
        viewModelScope.launch {
            _rssFeed.postValue(Resource.Loading())
            try {
                val rssFeed = iTunesApi.getRssFeed(url)
                _rssFeed.postValue(Resource.Success(rssFeed))
            } catch (e: Exception) {
                e.printStackTrace()
                _rssFeed.postValue(
                    Resource.Error(
                        e.localizedMessage ?: "Something went wrong"
                    )
                )
            }
        }
    }

    fun fetchRssFeedUrl(id: String) {
        viewModelScope.launch {
            try {
                val lookupResponse =
                    iTunesApi.getLookupResponse(BuildConfig.RSS_FEED_LOOKUP_URL, id)
                val rssFeedUrl = lookupResponse.lookupResults[0].feedUrl
                fetchRssFeed(rssFeedUrl)
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }
        }
    }

    fun searchPodcast(it: String) {
        viewModelScope.launch {
            _searchResponse.value = Resource.Loading()
            try {
                val searchResponse =
                    iTunesApi.getSearchResponse(
                        BuildConfig.RSS_FEED_SEARCH_URL,
                        COUNTRY,
                        SEARCH_MEDIA_PODCAST,
                        it
                    )
                _searchResponse.value = Resource.Success(searchResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
                _searchResponse.value = Resource.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }

    companion object {
        const val COUNTRY = "us"
        const val SEARCH_MEDIA_PODCAST = "podcast"
    }
}