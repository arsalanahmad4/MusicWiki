package com.example.musicwiki.api

import com.example.musicwiki.genredetails.albums.albumdetails.model.AlbumDetailsResponse
import com.example.musicwiki.genredetails.albums.model.TopAlbumsResponse
import com.example.musicwiki.genredetails.artists.artistdetails.model.ArtistDetailsResponse
import com.example.musicwiki.genredetails.artists.model.TopArtistResponse
import com.example.musicwiki.genredetails.model.GenreDetailResponse
import com.example.musicwiki.genredetails.tracks.model.TopTracksResponse
import com.example.musicwiki.model.AllGenresResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LastFMApi {

    @GET("2.0/")
    suspend fun getAllGenres(
        @Query("method")
        method: String = "tag.getTopTags"
    ): Response<AllGenresResponse>

    @GET("2.0/?method=tag.getinfo")
    suspend fun getGenreInfo(
        @Query("tag")
        tag: String
    ): Response<GenreDetailResponse>

    @GET("2.0/?method=tag.gettopalbums")
    suspend fun getTopAlbums(
        @Query("tag")
        tag:String,
        @Query("page")
        page:Int
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

    @GET("2.0/?method=album.getinfo")
    suspend fun getAlbumDetails(
        @Query("artist")
        artist:String,
        @Query("album")
        album:String
    ) :Response<AlbumDetailsResponse>

    @GET("2.0/?method=artist.getinfo")
    suspend fun getArtistDetails(
        @Query("artist")
        artist:String
    ) :Response<ArtistDetailsResponse>

    @POST("v2/event/custom")
    suspend fun customEventsApi(@Body branchEventRequest: BranchEventRequest):Response<BranchResponse>
}