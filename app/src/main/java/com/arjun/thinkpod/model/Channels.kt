package com.arjun.thinkpod.model

import com.prof.rssparser.Channel
import com.prof.rssparser.Image

object Channels {

    val channelList = listOf(

        Channel(
            title = "Naval",
            link = "https://naval.libsyn.com/rss",
            description = "Naval on wealth and happiness. On Twitter at @naval.",
            image = Image(
                title = "Naval",
                url = "https://ssl-static.libsyn.com/p/assets/5/c/e/b/5ceb9fba4ff0ab14/podcast_artwork.jpg",
                link = "https://nav.al",
            ),
            lastBuildDate = "Fri, 11 Sep 2020 14:36:41 +0000",
        ),
        Channel(
            title = "The Joe Rogan Experience",
            link = "http://joeroganexp.joerogan.libsynpro.com/rss",
            description = "The podcast of Comedian Joe Rogan..",
            image = Image(
                title = "The Joe Rogan Experience",
                url = "http://static.libsyn.com/p/assets/7/1/f/3/71f3014e14ef2722/JREiTunesImage2.jpg",
                link = "https://www.joerogan.com",
            ),
            lastBuildDate = "Thu, 17 Sep 2020 18:02:17 +0000",
        ),
        Channel(
            title = "The Daily",
            link = "http://rss.art19.com/the-daily",
            description = "This is what the news should sound like. The biggest stories of our time, told by the best journalists in the world. Hosted by Michael Barbaro. Twenty minutes a day, five days a week, ready by 6 a.m.",
            image = Image(
                title = "The Daily",
                url = "https://content.production.cdn.art19.com/images/01/1b/f3/d6/011bf3d6-a448-4533-967b-e2f19e376480/c81936f538106550b804e7e4fe2c236319bab7fba37941a6e8f7e5c3d3048b88fc5b2182fb790f7d446bdc820406456c94287f245db89d8656c105d5511ec3de.jpeg",
                link = "https://www.nytimes.com/the-daily",
            ),
        ),
        Channel(
            title = "The Ben Shapiro Show",
            link = "https://feeds.megaphone.fm/WWO8086402096",
            description = "Tired of the lies? Tired of the spin? Are you ready to hear the hard-hitting truth in comprehensive, conservative, principled fashion? The Ben Shapiro Show brings you all the news you need to know in the most fast moving daily program in America. Ben brutally breaks down the culture and never gives an inch! Monday thru Friday.",
            image = Image(
                title = "The Ben Shapiro Show",
                url = "https://images.megaphone.fm/_IaIovXFJT1vbHkbxi3QmrIR4mASd3rp156Qb26WJ2c/plain/s3://megaphone-prod/podcasts/35b42868-5c97-11ea-b0cc-039c766dfa49/image/avatars-000703722727-g9mf5u-original.jpg",
                link = "https://www.dailywire.com/podcasts/show/ben-shapiro-show",
            ),
        ),

        Channel(
            title = "Dateline NBC",
            link = "https://art19.com/shows/dateline-nbc",
            description = "Current and classic episodes, featuring compelling true-crime mysteries, powerful documentaries and in-depth investigations.",
            image = Image(
                title = "Dateline NBC",
                url = "https://content.production.cdn.art19.com/images/81/8f/a0/6a/818fa06a-b573-43c9-a870-fef30e9cac5e/7f0421f73d2ce0ca272e392c937e1a301285d44fe7c6d710c2844d80c0c7bb1a3e9838ac03ee80fc64199891cb9d5c6e9d4490f5081fb379c0ab2317f2cadf14.jpeg",
                link = "https://art19.com/shows/dateline-nbc",
            ),
        ),
    )
}


