package com.example.musicwiki.genredetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicwiki.MusicWikiRepository

class GenreViewModelProviderFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenreDetailsViewModel::class.java)) {
            return GenreDetailsViewModel(
                musicWikiRepository = MusicWikiRepository(), application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}