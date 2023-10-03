package com.example.musicwiki.genredetails

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.musicwiki.MusicWikiApplication
import com.example.musicwiki.MusicWikiRepository
import com.example.musicwiki.api.BranchResponse
import com.example.musicwiki.genredetails.albums.albumdetails.model.AlbumDetailsResponse
import com.example.musicwiki.genredetails.albums.model.TopAlbumsResponse
import com.example.musicwiki.genredetails.artists.artistdetails.model.ArtistDetailsResponse
import com.example.musicwiki.genredetails.artists.model.TopArtistResponse
import com.example.musicwiki.genredetails.model.GenreDetailResponse
import com.example.musicwiki.genredetails.tracks.model.TopTracksResponse
import com.example.musicwiki.util.Resource
import com.example.musicwiki.util.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class GenreDetailsViewModel(
    private val musicWikiRepository: MusicWikiRepository,
    app: Application
) : AndroidViewModel(app) {

    private val _genreInfoLiveData = MutableLiveData<Resource<GenreDetailResponse>>()
    val genreInfoLiveData: LiveData<Resource<GenreDetailResponse>> = _genreInfoLiveData

    private val _topAlbumsLiveData = MutableLiveData<Resource<TopAlbumsResponse>>()
    val topAlbumsLiveData: LiveData<Resource<TopAlbumsResponse>> = _topAlbumsLiveData

    private val _topArtistsLiveData = MutableLiveData<Resource<TopArtistResponse>>()
    val topArtistsLiveData: LiveData<Resource<TopArtistResponse>> = _topArtistsLiveData

    private val _topTracksLiveData = MutableLiveData<Resource<TopTracksResponse>>()
    val topTracksLiveData: LiveData<Resource<TopTracksResponse>> = _topTracksLiveData

    private val _getAlbumDetailsLiveData = MutableLiveData<Resource<AlbumDetailsResponse>>()
    val getAlbumDetailsLiveData: LiveData<Resource<AlbumDetailsResponse>> = _getAlbumDetailsLiveData

    private val _getArtistDetailsLiveData = MutableLiveData<Resource<ArtistDetailsResponse>>()
    val getArtistDetailsLiveData: LiveData<Resource<ArtistDetailsResponse>> =
        _getArtistDetailsLiveData

    private val _postBranchEventLiveData = MutableLiveData<Resource<BranchResponse>>()
    val postBranchEventLiveData: LiveData<Resource<BranchResponse>> =
        _postBranchEventLiveData


    fun getGenreInfo(tag: String) {
        viewModelScope.launch {
            safeGetGenreInfoCall(tag)
        }
    }

    fun getTopAlbums(tag: String, page: Int) {
        viewModelScope.launch {
            safeGetTopAlbumsCall(tag, page)
        }
    }

    fun getTopArtist(tag: String) {
        viewModelScope.launch {
            safeGetTopArtistsCall(tag)
        }
    }

    fun getTopTracks(tag: String) {
        viewModelScope.launch {
            safeGetTopTracksCall(tag)
        }
    }

    fun getArtistDetails(artist: String) {
        viewModelScope.launch {
            safeGetArtistDetailsCall(artist)
        }
    }

    fun getAlbumDetails(artist: String, album: String) {
        viewModelScope.launch {
            safeGetAlbumDetailsCall(artist, album)
        }
    }

    fun postCustomEvent(
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
    ) {
        viewModelScope.launch {
            safePostBranchCustomEvent(
                branchKey = branchKey,
                eventName = eventName,
                eventAlias = eventAlias,
                link = link,
                artist = artist,
                album = album,
                eventDescription = eventDescription,
                eventSearchQuery = eventSearchQuery,
                androidId = androidId,
                localIp = localIp
            )
        }
    }

    private suspend fun safeGetGenreInfoCall(tag: String) {
        _genreInfoLiveData.postValue(Resource.Loading())
        try {
            val connectivityManager = getApplication<MusicWikiApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (hasInternetConnection(connectivityManager)) {
                val response = musicWikiRepository.getGenreInfo(tag)
                _genreInfoLiveData.postValue(handleGenreInfoApiResponse(response))
            } else {
                _genreInfoLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _genreInfoLiveData.postValue(Resource.Error("Network Failure"))
                else -> _genreInfoLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleGenreInfoApiResponse(response: Response<GenreDetailResponse>): Resource<GenreDetailResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeGetTopAlbumsCall(tag: String, page: Int) {
        _topAlbumsLiveData.postValue(Resource.Loading())
        try {
            val connectivityManager = getApplication<MusicWikiApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (hasInternetConnection(connectivityManager)) {
                val response = musicWikiRepository.getTopAlbums(tag, page)
                _topAlbumsLiveData.postValue(handleTopAlbumsApiResponse(response))
            } else {
                _topAlbumsLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _topAlbumsLiveData.postValue(Resource.Error("Network Failure"))
                else -> _topAlbumsLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleTopAlbumsApiResponse(response: Response<TopAlbumsResponse>): Resource<TopAlbumsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeGetTopArtistsCall(tag: String) {
        _topArtistsLiveData.postValue(Resource.Loading())
        try {
            val connectivityManager = getApplication<MusicWikiApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (hasInternetConnection(connectivityManager)) {
                val response = musicWikiRepository.getTopArtist(tag)
                _topArtistsLiveData.postValue(handleTopArtistsApiResponse(response))
            } else {
                _topArtistsLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _topArtistsLiveData.postValue(Resource.Error("Network Failure"))
                else -> _topArtistsLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleTopArtistsApiResponse(response: Response<TopArtistResponse>): Resource<TopArtistResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeGetTopTracksCall(tag: String) {
        _topTracksLiveData.postValue(Resource.Loading())
        try {
            val connectivityManager = getApplication<MusicWikiApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (hasInternetConnection(connectivityManager)) {
                val response = musicWikiRepository.getTopTracks(tag)
                _topTracksLiveData.postValue(handleTopTracksApiResponse(response))
            } else {
                _topTracksLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _topTracksLiveData.postValue(Resource.Error("Network Failure"))
                else -> _topTracksLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleTopTracksApiResponse(response: Response<TopTracksResponse>): Resource<TopTracksResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeGetAlbumDetailsCall(artist: String, album: String) {
        _getAlbumDetailsLiveData.postValue(Resource.Loading())
        try {
            val connectivityManager = getApplication<MusicWikiApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (hasInternetConnection(connectivityManager)) {
                val response = musicWikiRepository.getAlbumDetails(artist, album)
                _getAlbumDetailsLiveData.postValue(handleAlbumDetailsApiResponse(response))
            } else {
                _getAlbumDetailsLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _getAlbumDetailsLiveData.postValue(Resource.Error("Network Failure"))
                else -> _getAlbumDetailsLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleAlbumDetailsApiResponse(response: Response<AlbumDetailsResponse>): Resource<AlbumDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeGetArtistDetailsCall(artist: String) {
        _getArtistDetailsLiveData.postValue(Resource.Loading())
        try {
            val connectivityManager = getApplication<MusicWikiApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (hasInternetConnection(connectivityManager)) {
                val response = musicWikiRepository.getArtistDetails(artist)
                _getArtistDetailsLiveData.postValue(handleArtistDetailsApiResponse(response))
            } else {
                _getArtistDetailsLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _getArtistDetailsLiveData.postValue(Resource.Error("Network Failure"))
                else -> _getArtistDetailsLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleArtistDetailsApiResponse(response: Response<ArtistDetailsResponse>): Resource<ArtistDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safePostBranchCustomEvent(
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
    ) {
        _postBranchEventLiveData.postValue(Resource.Loading())
        try {
            val connectivityManager = getApplication<MusicWikiApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (hasInternetConnection(connectivityManager)) {
                val response = musicWikiRepository.postBranchEvent(
                    branchKey = branchKey,
                    eventName = eventName,
                    eventAlias = eventAlias,
                    link = link,
                    artist = artist,
                    album = album,
                    eventDescription = eventDescription,
                    eventSearchQuery = eventSearchQuery,
                    androidId = androidId,
                    localIp = localIp
                )
                _postBranchEventLiveData.postValue(handleBranchEventResponse(response))
            } else {
                _postBranchEventLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _postBranchEventLiveData.postValue(Resource.Error("Network Failure"))
                else -> _postBranchEventLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleBranchEventResponse(response: Response<BranchResponse>): Resource<BranchResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}