package com.example.musicwiki.genredetails.tracks.model

import com.google.gson.annotations.SerializedName

data class Streamable(
    @SerializedName("#text") val text: String,
    val fulltrack: String
)