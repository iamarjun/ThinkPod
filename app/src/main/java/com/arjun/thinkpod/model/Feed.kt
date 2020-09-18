package com.arjun.thinkpod.model

import com.google.gson.annotations.SerializedName

data class Feed(
    @SerializedName("title")
    val title: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("results")
    val podCasts: List<Podcast>,
)
