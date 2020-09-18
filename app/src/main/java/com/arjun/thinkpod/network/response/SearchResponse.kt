package com.arjun.thinkpod.network.response

import com.arjun.thinkpod.model.SearchResult
import com.google.gson.annotations.SerializedName

class SearchResponse {
    @SerializedName("results")
    var searchResults: List<SearchResult>? = null
}