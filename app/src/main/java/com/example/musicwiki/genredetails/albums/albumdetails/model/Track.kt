package com.example.musicwiki.genredetails.albums.albumdetails.model

import com.google.gson.annotations.SerializedName

data class Track(
    val artist: Artist,
    val duration: Int,
    val name: String,
    val url: String
)