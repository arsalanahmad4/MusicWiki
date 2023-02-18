package com.example.musicwiki.api

import com.example.musicwiki.model.AllGenresResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFMApi {

    @GET("2.0/")
    suspend fun getAllGenres(
        @Query("method")
        method: String = "tag.getTopTags"
    ): Response<AllGenresResponse>
}