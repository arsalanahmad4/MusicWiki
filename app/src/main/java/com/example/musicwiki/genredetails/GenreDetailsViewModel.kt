package com.example.musicwiki.genredetails

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicwiki.MusicWikiApplication
import com.example.musicwiki.MusicWikiRepository
import com.example.musicwiki.genredetails.albums.model.TopAlbumsResponse
import com.example.musicwiki.genredetails.artists.model.TopArtistResponse
import com.example.musicwiki.genredetails.tracks.model.TopTracksResponse
import com.example.musicwiki.model.AllGenresResponse
import com.example.musicwiki.util.Resource
import com.example.musicwiki.util.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class GenreDetailsViewModel(private val musicWikiRepository: MusicWikiRepository, app: Application) : AndroidViewModel(app) {

    private val _topAlbumsLiveData = MutableLiveData<Resource<TopAlbumsResponse>>()
    val topAlbumsLiveData: LiveData<Resource<TopAlbumsResponse>> = _topAlbumsLiveData

    private val _topArtistsLiveData = MutableLiveData<Resource<TopArtistResponse>>()
    val topArtistsLiveData: LiveData<Resource<TopArtistResponse>> = _topArtistsLiveData

    private val _topTracksLiveData = MutableLiveData<Resource<TopTracksResponse>>()
    val topTracksLiveData: LiveData<Resource<TopTracksResponse>> = _topTracksLiveData


    fun getTopAlbums(tag:String){
        viewModelScope.launch {
            safeGetTopAlbumsCall(tag)
        }
    }

    fun getTopArtist(tag:String){
        viewModelScope.launch {
            safeGetTopArtistsCall(tag)
        }
    }

    fun getTopTracks(tag:String){
        viewModelScope.launch {
            safeGetTopTracksCall(tag)
        }
    }

    private suspend fun safeGetTopAlbumsCall(tag:String) {
        _topAlbumsLiveData.postValue(Resource.Loading())
        try {
            val connectivityManager = getApplication<MusicWikiApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (hasInternetConnection(connectivityManager)) {
                val response = musicWikiRepository.getTopAlbums(tag)
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

    private suspend fun safeGetTopArtistsCall(tag:String) {
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

    private suspend fun safeGetTopTracksCall(tag:String) {
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
}