package com.example.musicwiki.genredetails.albums.albumdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicwiki.R
import com.example.musicwiki.genredetails.GenreDetailsViewModel
import com.example.musicwiki.genredetails.GenreViewModelProviderFactory
import com.example.musicwiki.util.Resource

class AlbumDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: GenreDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, GenreViewModelProviderFactory(application))[GenreDetailsViewModel::class.java]
        setContentView(R.layout.activity_album_details)
        bindView()
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.getAlbumDetailsLiveData.observe(this@AlbumDetailsActivity, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { allGenres ->

                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->

                    }
                }
                is Resource.Loading -> {

                }
            }
        })
    }

    private fun bindView() {
        val artist = intent.extras?.getString("artist")
        val album = intent.extras?.getString("album")
        if(!artist.isNullOrEmpty() && !album.isNullOrEmpty()){
            viewModel.getAlbumDetails(artist,album)

        }
    }
}