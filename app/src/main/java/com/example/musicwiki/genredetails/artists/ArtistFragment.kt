package com.example.musicwiki.genredetails.artists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.musicwiki.R
import com.example.musicwiki.genredetails.GenreDetailsActivity
import com.example.musicwiki.genredetails.GenreDetailsViewModel
import com.example.musicwiki.util.Constants
import com.example.musicwiki.util.Resource

class ArtistFragment : Fragment() {

    lateinit var viewModel: GenreDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as GenreDetailsActivity).viewModel
        arguments?.getString(Constants.BUNDLE_KEY_GENRE_NAME)?.let { viewModel.getTopArtist(it) }
        bindObserver()
    }

    private fun bindObserver(){
        viewModel.topArtistsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { artistsInfo ->

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
}