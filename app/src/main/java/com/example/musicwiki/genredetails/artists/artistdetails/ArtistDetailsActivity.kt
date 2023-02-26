package com.example.musicwiki.genredetails.artists.artistdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicwiki.R
import com.example.musicwiki.genredetails.GenreDetailsViewModel
import com.example.musicwiki.genredetails.GenreViewModelProviderFactory
import com.example.musicwiki.util.Resource

class ArtistDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: GenreDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, GenreViewModelProviderFactory(application))[GenreDetailsViewModel::class.java]
        setContentView(R.layout.activity_artist_details)
        bindView()
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.getAlbumDetailsLiveData.observe(this@ArtistDetailsActivity, Observer { response ->
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
        viewModel.getArtistDetails("")
    }
}