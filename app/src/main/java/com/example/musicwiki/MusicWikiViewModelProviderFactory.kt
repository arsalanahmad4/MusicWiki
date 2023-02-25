package com.example.musicwiki

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class MusicWikiViewModelProviderFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicWikiViewModel::class.java)) {
            return MusicWikiViewModel(
                musicWikiRepository = MusicWikiRepository(), application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}