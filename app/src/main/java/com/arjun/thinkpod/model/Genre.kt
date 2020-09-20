package com.arjun.thinkpod.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(
    @SerializedName("genreId")
    val genreId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String,
) : Parcelable