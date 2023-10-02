package com.example.musicwiki.genredetails.artists.artistdetails.model

import com.google.gson.annotations.SerializedName

data class Link(
    @SerializedName("#text") val text: String?,
    val href: String?,
    val rel: String?
)