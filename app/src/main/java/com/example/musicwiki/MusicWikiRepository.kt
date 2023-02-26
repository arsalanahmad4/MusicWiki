package com.example.musicwiki

import com.example.musicwiki.api.RetrofitInstance

class MusicWikiRepository {

    suspend fun getAllGenres() = RetrofitInstance.api.getAllGenres()

    suspend fun getGenreInfo(tag:String) = RetrofitInstance.api.getGenreInfo(tag)
    suspend fun getTopAlbums(tag:String) = RetrofitInstance.api.getTopAlbums(tag)

    suspend fun getTopArtist(tag:String) = RetrofitInstance.api.getTopArtist(tag)

    suspend fun getTopTracks(tag:String) = RetrofitInstance.api.getTopTracks(tag)

    suspend fun getAlbumDetails(artist:String,album:String) = RetrofitInstance.api.getAlbumDetails(artist,album)

    suspend fun getArtistDetails(artist:String) = RetrofitInstance.api.getArtistDetails(artist)
}