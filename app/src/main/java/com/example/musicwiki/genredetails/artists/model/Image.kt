package com.example.musicwiki.genredetails.artists.model

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("#text") val text: String,
    val size: String
)