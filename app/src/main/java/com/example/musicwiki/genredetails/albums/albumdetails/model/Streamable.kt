package com.example.musicwiki.genredetails.albums.albumdetails.model

import com.google.gson.annotations.SerializedName

data class Streamable(
    @SerializedName("#text") val text: String,
    val fulltrack: String
)