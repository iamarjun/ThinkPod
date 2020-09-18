package com.arjun.thinkpod.network

import com.arjun.thinkpod.model.RssFeed
import com.arjun.thinkpod.network.response.ITunesResponse
import com.arjun.thinkpod.network.response.LookupResponse
import com.arjun.thinkpod.network.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ITunesApi {
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Json

    @Retention(AnnotationRetention.RUNTIME)
    annotation class Xml

    @Json
    @GET("{country}/podcasts/top-podcasts/all/50/explicit.json")
    suspend fun getTopPodcasts(
        @Path("country") country: String
    ): ITunesResponse

    /**
     * Reference: @see "https://stackoverflow.com/questions/32559333/retrofit-2-dynamic-url"
     *
     * @param url @Url parameter annotation allows passing a complete URL for an endpoint.
     * will overrid the baseUrl anyway
     * @param id  The id is used to create a lookup request to search for a specific podcast
     */
    @GET
    @Json
    suspend fun getLookupResponse(
        @Url url: String,
        @Query("id") id: String
    ): LookupResponse

    @GET
    @Xml
    suspend fun getRssFeed(
        @Url url: String
    ): RssFeed

    @GET
    @Json
    suspend fun getSearchResponse(
        @Url searchUrl: String,
        @Query("country") country: String,
        @Query("media") media: String,
        @Query("term") term: String
    ): SearchResponse
}