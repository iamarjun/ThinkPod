package com.arjun.thinkpod.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("feedUrl")
    val feedUrl: String,

    /** The Podcast ID  */
    @SerializedName("collectionId")
    val collectionId: Int,

    @SerializedName("artistName")
    val artistName: String,

    /** The Podcast Name  */
    @SerializedName("collectionName")
    val collectionName: String,

    @SerializedName("artworkUrl600")
    val artworkUrl600: String,
)