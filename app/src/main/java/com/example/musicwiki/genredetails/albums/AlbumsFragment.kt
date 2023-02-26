package com.example.musicwiki.genredetails.albums

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

class AlbumsFragment : Fragment() {

    lateinit var viewModel: GenreDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as GenreDetailsActivity).viewModel
        arguments?.getString(Constants.BUNDLE_KEY_GENRE_NAME)?.let { viewModel.getTopAlbums(it) }
        bindObserver()
    }

    private fun bindObserver(){
        viewModel.topAlbumsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { albumsInfo ->

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