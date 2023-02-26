package com.example.musicwiki

import com.example.musicwiki.api.RetrofitInstance

class MusicWikiRepository {

    suspend fun getAllGenres() = RetrofitInstance.api.getAllGenres()

    suspend fun getTopAlbums(tag:String) = RetrofitInstance.api.getTopAlbums(tag)

    suspend fun getTopArtist(tag:String) = RetrofitInstance.api.getTopArtist(tag)

    suspend fun getTopTracks(tag:String) = RetrofitInstance.api.getTopTracks(tag)
}