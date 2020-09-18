package com.arjun.thinkpod.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("genreId")
    val genreId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String,
)