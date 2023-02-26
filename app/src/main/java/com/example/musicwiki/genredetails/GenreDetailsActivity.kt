package com.example.musicwiki.genredetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.musicwiki.MusicWikiViewModel
import com.example.musicwiki.MusicWikiViewModelProviderFactory
import com.example.musicwiki.R
import com.example.musicwiki.databinding.ActivityGenreDetailsBinding
import com.example.musicwiki.genredetails.albums.AlbumsFragment
import com.example.musicwiki.genredetails.artists.ArtistFragment
import com.example.musicwiki.genredetails.tracks.TracksFragment
import com.example.musicwiki.util.Constants
import com.example.musicwiki.util.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GenreDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenreDetailsBinding

    lateinit var viewModel: GenreDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, GenreViewModelProviderFactory(application))[GenreDetailsViewModel::class.java]
        binding = ActivityGenreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = intent.extras?.getString(Constants.BUNDLE_KEY_GENRE_NAME)
        bindView()
        bindObservers()
    }

    private fun bindView(){
        viewModel.getGenreInfo(intent.extras?.getString(Constants.BUNDLE_KEY_GENRE_NAME)?:"")
        val fragmentList = arrayListOf<Fragment>(AlbumsFragment(),ArtistFragment(), TracksFragment())
        val viewPagerAdapter = ViewPagerAdapter(intent.extras?.getString(Constants.BUNDLE_KEY_GENRE_NAME)?:"",fragmentList, supportFragmentManager,lifecycle)
        binding.pager.adapter =viewPagerAdapter
        val titleList = arrayListOf<String>("Albums","Artists","Tracks")
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

    private fun bindObservers(){
        viewModel.genreInfoLiveData.observe(this@GenreDetailsActivity, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { genreInfo ->

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