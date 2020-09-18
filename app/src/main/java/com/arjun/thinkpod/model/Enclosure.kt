package com.arjun.thinkpod.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Parcelize
@Root(name = "enclosure", strict = false)
data class Enclosure(
    @Attribute(name = "url", required = false)
    val url: String,

    @Attribute(name = "type", required = false)
    val type: String,

    @Attribute(name = "length", required = false)
    val length: String,
) : Parcelable