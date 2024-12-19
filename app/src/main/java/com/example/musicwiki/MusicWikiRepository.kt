package com.example.musicwiki

import com.example.musicwiki.api.BranchEventRequest
import com.example.musicwiki.api.BranchResponse
import com.example.musicwiki.api.CustomData
import com.example.musicwiki.api.EventData
import com.example.musicwiki.api.MetaData
import com.example.musicwiki.api.RetrofitInstance
import com.example.musicwiki.api.UserData
import retrofit2.Response

class MusicWikiRepository {

    suspend fun getAllGenres() = RetrofitInstance.api.getAllGenres()

    suspend fun getGenreInfo(tag: String) = RetrofitInstance.api.getGenreInfo(tag)
    suspend fun getTopAlbums(tag: String, page: Int) = RetrofitInstance.api.getTopAlbums(tag, page)

    suspend fun getTopArtist(tag: String) = RetrofitInstance.api.getTopArtist(tag)

    suspend fun getTopTracks(tag: String) = RetrofitInstance.api.getTopTracks(tag)

    suspend fun getAlbumDetails(artist: String, album: String) =
        RetrofitInstance.api.getAlbumDetails(artist, album)

    suspend fun getArtistDetails(artist: String) = RetrofitInstance.api.getArtistDetails(artist)

    suspend fun postBranchEvent(
        branchKey: String,
        eventName: String,
        eventAlias: String,
        link: String,
        artist: String,
        album: String? = null,
        eventDescription: String,
        eventSearchQuery: String,
        androidId: String,
        localIp: String
    ): Response<BranchResponse> {
        val customData = CustomData(CreatedLink = link, artist = artist, album = album)
        val eventData = EventData(description = eventDescription, search_query = eventSearchQuery)
        val meta = MetaData(androidId,androidId)
        val userData = UserData(android_id = androidId, local_ip = localIp)
        val request = BranchEventRequest(
            branch_key = branchKey,
            name = eventName,
            customer_event_alias = eventAlias,
            custom_data = customData,
            event_data = eventData,
            user_data = userData,
            metadata = meta
        )
        return RetrofitInstance.branchApi.customEventsApi(request)
    }
}