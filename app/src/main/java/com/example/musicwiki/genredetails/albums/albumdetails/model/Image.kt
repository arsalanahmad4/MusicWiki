package com.example.musicwiki.genredetails.albums.albumdetails.model

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("#text") val text: String,
    val size: String
)