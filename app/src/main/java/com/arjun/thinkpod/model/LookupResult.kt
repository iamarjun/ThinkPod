package com.arjun.thinkpod.model

import com.google.gson.annotations.SerializedName

data class LookupResult(
    @SerializedName("feedUrl")
    val feedUrl: String
)