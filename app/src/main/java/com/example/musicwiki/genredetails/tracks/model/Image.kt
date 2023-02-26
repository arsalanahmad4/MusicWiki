package com.example.musicwiki.genredetails.tracks.model

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("#text") val text: String,
    val size: String
)