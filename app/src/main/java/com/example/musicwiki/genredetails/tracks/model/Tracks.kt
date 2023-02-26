package com.example.musicwiki.genredetails.tracks.model

import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("@attr") val attr: Attr,
    val track: List<Track>
)