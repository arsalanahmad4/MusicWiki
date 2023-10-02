package com.example.musicwiki.genredetails.albums.model

import com.google.gson.annotations.SerializedName

data class Album(
    val artist: Artist,
    val image: List<Image>,
    val mbid: String,
    val name: String,
    val url: String
)