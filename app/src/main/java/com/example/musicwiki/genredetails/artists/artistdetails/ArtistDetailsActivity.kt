package com.example.musicwiki.genredetails.artists.artistdetails

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicwiki.R
import com.example.musicwiki.databinding.ActivityArtistDetailsBinding
import com.example.musicwiki.genredetails.GenreDetailsViewModel
import com.example.musicwiki.genredetails.GenreViewModelProviderFactory
import com.example.musicwiki.genredetails.artists.artistdetails.model.ArtistDetailsResponse
import com.example.musicwiki.genredetails.artists.artistdetails.model.Tag
import com.example.musicwiki.util.Resource
import com.example.musicwiki.util.openUrlInCustomTabIntent
import com.google.android.material.chip.Chip

class ArtistDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: GenreDetailsViewModel

    private lateinit var binding: ActivityArtistDetailsBinding

    private var artist :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, GenreViewModelProviderFactory(application))[GenreDetailsViewModel::class.java]
        binding = ActivityArtistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        artist = intent.extras?.getString("artist")
        binding.toolbar.toolbarBack.setOnClickListener{
            onBackPressed()
        }
        binding.toolbar.tvBrandName.text = artist
        bindView()
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.getArtistDetailsLiveData.observe(this@ArtistDetailsActivity, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { artistDetails ->
                        setApiResponseData(artistDetails)
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
        artist?.let { viewModel.getArtistDetails(it) }
    }

    private fun setApiResponseData(artistDetails: ArtistDetailsResponse) {
        binding.tvTotalListenersCount.text = artistDetails.artist.stats.listeners
        binding.tvPlayCount.text = artistDetails.artist.stats.playcount
        setDetailsText(artistDetails.artist.bio.content)
        addChipsFromList(artistDetails.artist.tags.tag)
        binding.tvVisitWebsite.setOnClickListener {
            openUrlInCustomTabIntent(artistDetails.artist.url,this)
        }
    }

    private fun setDetailsText(artistDetails: String) {

        var myShortenedText = ""
        binding.tvCampaignDetail.text = artistDetails;
        binding.tvCampaignDetail.post {
            // Past the maximum number of lines we want to display.
            if (binding.tvCampaignDetail.lineCount > 3) {
                val lastCharShown = binding.tvCampaignDetail.layout.getLineVisibleEnd(3 - 1);

                binding.tvCampaignDetail.maxLines = 3;
                val actionDisplayText = artistDetails.substring(0, lastCharShown) + "   "

                myShortenedText = actionDisplayText
                // Set the truncated text to the TextView
                binding.tvCampaignReadMore.visibility = View.VISIBLE
                binding.tvCampaignDetail.text = actionDisplayText;
            } else {
                myShortenedText = artistDetails
            }
        }
        binding.tvCampaignReadMore.setOnClickListener {
            // Handle click event here, for example, show the full text.
            binding.tvCampaignDetail.maxLines = Integer.MAX_VALUE
            binding.tvCampaignDetail.text = artistDetails

            binding.tvCampaignReadMore.visibility = View.GONE
            binding.tvCampaignReadLess.visibility = View.VISIBLE
        }
        binding.tvCampaignReadLess.setOnClickListener {
            // Handle click event here, for example, show the full text.
            binding.tvCampaignDetail.maxLines = 3
            binding.tvCampaignDetail.text = myShortenedText
            binding.tvCampaignReadMore.visibility = View.VISIBLE
            binding.tvCampaignReadLess.visibility = View.GONE
        }
    }

    private fun addChipsFromList(tag: List<Tag>) {
        binding.llSelectionTabs?.removeAllViews()
        for ((index, slot) in tag.withIndex()) {
            if (slot != null) {
                val chip = Chip(this,null, R.attr.CustomChipChoiceStyle)
                chip.text = slot.name
                chip.isCheckable = true

                // Set a click listener for the close icon
                binding.llSelectionTabs.addView(chip)
            }
        }
    }
}