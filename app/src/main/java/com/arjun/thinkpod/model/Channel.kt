package com.arjun.thinkpod.model

import org.simpleframework.xml.*

//strict = false, means that we don't mind skipping some elements provided in the XML inside this root
@Root(name = "channel", strict = false)
class Channel(
    @ElementList(inline = true, name = "image", required = false)
    val artworkImages: List<ArtworkImage>,

    @Element(name = "title", required = false)
    val title: String = "",

    @Path("description")
    @Text(required = false)
    val description: String = "",

    @Path("author")
    @Text(required = false)
    val iTunesAuthor: String = "",

    @Element(name = "language", required = false)
    val language: String = "",

    @ElementList(inline = true, name = "category", required = false)
    val categories: List<Category>,

    @ElementList(inline = true, name = "item", required = false)
    val mItemList: List<Item>,
)