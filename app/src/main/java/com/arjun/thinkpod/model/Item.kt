package com.arjun.thinkpod.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.simpleframework.xml.*
import java.util.*

@Parcelize
@Root(name = "item", strict = false)
data class Item(
    val id: String = UUID.randomUUID().toString(),

    @Path("title") //this is not the only way , we could have added @Element
    @Text(required = false)
    val title: String,

    @Path("description") //this is not the only way , we could have added @Element
    @Text(required = false)
    val description: String,

    @Element(name = "summary", required = false)
    val iTunesSummary: String,

    @Element(name = "pubDate", required = false)
    val pubDate: String,

    @Element(name = "duration", required = false)
    val iTunesDuration: String,

    @ElementList(inline = true, name = "enclosure", required = false)
    val enclosures: List<Enclosure>,

    @ElementList(inline = true, name = "image", required = false)
    val itemImages: List<ItemImage>,
) : Parcelable