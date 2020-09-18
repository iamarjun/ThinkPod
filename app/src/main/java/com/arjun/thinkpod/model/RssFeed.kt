package com.arjun.thinkpod.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class RssFeed(
    @Element(name = "channel", required = false)
    val channel: Channel
)