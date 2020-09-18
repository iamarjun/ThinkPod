package com.arjun.thinkpod.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Podcast(
    @SerializedName("artistName")
    val artistName: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("releaseDate")
    val releaseDate: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("kind")
    val kind: String,

    @SerializedName("copyright")
    val copyright: String,

    @SerializedName("artistId")
    val artistId: String,

    @SerializedName("contentAdvisoryRating")
    val contentAdvisoryRating: String,

    @SerializedName("artistUrl")
    val artistUrl: String,

    @SerializedName("artworkUrl100")
    val artworkUrl: String,

    @SerializedName("genres")
    val genres: @RawValue List<Genre>,

    @SerializedName("url")
    val url: String,
) : Parcelable