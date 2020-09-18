package com.arjun.thinkpod.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "image", strict = false)
data class ArtworkImage(
    @Attribute(name = "href", required = false)
    val imageHref: String,

    @Element(name = "url", required = false)
    val imageUrl: String,
)