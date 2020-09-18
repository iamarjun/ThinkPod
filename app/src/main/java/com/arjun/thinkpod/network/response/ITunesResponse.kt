package com.arjun.thinkpod.network.response

import com.arjun.thinkpod.model.Feed
import com.google.gson.annotations.SerializedName

data class ITunesResponse(
    @SerializedName("feed")
    val feed: Feed
)