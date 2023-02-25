package com.example.musicwiki

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicwiki.model.AllGenresResponse
import com.example.musicwiki.util.Resource
import com.example.musicwiki.util.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MusicWikiViewModel(private val musicWikiRepository: MusicWikiRepository, app: Application) :
    AndroidViewModel(app) {

    private val _allGenreLiveData = MutableLiveData<Resource<AllGenresResponse>>()
    val allGenreLiveData: LiveData<Resource<AllGenresResponse>> = _allGenreLiveData


    fun getAllGenre(){
        viewModelScope.launch {
            safeGetAllGenreCall()
        }
    }

    private suspend fun safeGetAllGenreCall() {
        _allGenreLiveData.postValue(Resource.Loading())
        try {
            val connectivityManager = getApplication<MusicWikiApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (hasInternetConnection(connectivityManager)) {
                val response = musicWikiRepository.getAllGenres()
                _allGenreLiveData.postValue(handleAllGenresApiResponse(response))
            } else {
                _allGenreLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _allGenreLiveData.postValue(Resource.Error("Network Failure"))
                else -> _allGenreLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleAllGenresApiResponse(response: Response<AllGenresResponse>): Resource<AllGenresResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}