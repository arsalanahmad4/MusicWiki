package com.example.musicwiki.api

import com.example.musicwiki.genredetails.albums.model.TopAlbumsResponse
import com.example.musicwiki.genredetails.artists.model.TopArtistResponse
import com.example.musicwiki.genredetails.tracks.model.TopTracksResponse
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
    @GET("2.0/?method=tag.gettopalbums")
    suspend fun getTopAlbums(
        @Query("tag")
        tag:String
    ) :Response<TopAlbumsResponse>

    @GET("2.0/?method=tag.gettopartists")
    suspend fun getTopArtist(
        @Query("tag")
        tag:String
    ) :Response<TopArtistResponse>

    @GET("2.0/?method=tag.gettoptracks")
    suspend fun getTopTracks(
        @Query("tag")
        tag:String
    ) :Response<TopTracksResponse>
}