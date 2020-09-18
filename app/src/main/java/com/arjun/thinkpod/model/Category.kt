package com.arjun.thinkpod.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "category", strict = false)
data class Category(
    @Attribute(name = "text", required = false)
    val text: String = "",
)