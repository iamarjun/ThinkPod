package com.arjun.thinkpod.network.response

import com.arjun.thinkpod.model.LookupResult
import com.google.gson.annotations.SerializedName

data class LookupResponse(
    @SerializedName("results")
    val lookupResults: List<LookupResult>
)