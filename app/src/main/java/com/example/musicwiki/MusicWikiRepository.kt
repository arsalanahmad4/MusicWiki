package com.example.musicwiki

import com.example.musicwiki.api.RetrofitInstance

class MusicWikiRepository {

    suspend fun getAllGenres() = RetrofitInstance.api.getAllGenres()
}