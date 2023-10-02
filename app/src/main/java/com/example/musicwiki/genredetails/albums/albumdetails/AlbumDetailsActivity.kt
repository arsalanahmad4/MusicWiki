package com.example.musicwiki.genredetails.albums.albumdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicwiki.util.ActivityNavigator
import com.example.musicwiki.R
import com.example.musicwiki.databinding.ActivityAlbumDetailsBinding
import com.example.musicwiki.genredetails.GenreDetailsViewModel
import com.example.musicwiki.genredetails.GenreViewModelProviderFactory
import com.example.musicwiki.genredetails.albums.albumdetails.model.AlbumDetailsResponse
import com.example.musicwiki.genredetails.albums.albumdetails.model.Tag
import com.example.musicwiki.util.Resource
import com.example.musicwiki.util.openUrlInCustomTabIntent
import com.example.musicwiki.util.shareLink
import com.google.android.material.chip.Chip
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.LinkProperties

class AlbumDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: GenreDetailsViewModel

    private lateinit var binding: ActivityAlbumDetailsBinding

    private var artist :String? = null
    private var album :String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, GenreViewModelProviderFactory(application))[GenreDetailsViewModel::class.java]
        binding = ActivityAlbumDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        artist = intent.extras?.getString("artist")
        album = intent.extras?.getString("album")

        binding.toolbar.toolbarBack.setOnClickListener{
            onBackPressed()
        }
        binding.toolbar.tvBrandName.text = artist
        binding.tvAlbumName.text = album
        bindView()
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.getAlbumDetailsLiveData.observe(this@AlbumDetailsActivity, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { albumDetails ->
                        setApiResponseData(albumDetails)
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
        if(!artist.isNullOrEmpty() && !album.isNullOrEmpty()){
            viewModel.getAlbumDetails(artist!!, album!!)
        }
    }

    private fun setApiResponseData(albumDetails: AlbumDetailsResponse) {
        binding.tvTotalListenersCount.text = albumDetails.album.listeners
        binding.tvPlayCount.text = albumDetails.album.playcount
        setDetailsText(albumDetails.album.wiki.summary)
        addChipsFromList(albumDetails.album.tags.tag)
        binding.tvVisitWebsite.setOnClickListener {
            openUrlInCustomTabIntent(albumDetails.album.url,this)
        }
        binding.toolbar.llLinkShare.setOnClickListener{
            createShareLink(albumDetails.album.url,albumDetails.album.name,albumDetails.album.artist,albumDetails.album.image.get(0).text)
        }
    }

    private fun addChipsFromList(tag: List<Tag>) {
        binding.llSelectionTabs.removeAllViews()
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


    private fun setDetailsText(albumDetails: String) {

        var myShortenedText = ""
        binding.tvDetails.text = albumDetails;
        binding.tvDetails.post {
            // Past the maximum number of lines we want to display.
            if (binding.tvDetails.lineCount > 3) {
                val lastCharShown = binding.tvDetails.layout.getLineVisibleEnd(3 - 1);

                binding.tvDetails.maxLines = 3;
                val actionDisplayText = albumDetails.substring(0, lastCharShown) + "   "

                myShortenedText = actionDisplayText
                // Set the truncated text to the TextView
                binding.tvReadMore.visibility = View.VISIBLE
                binding.tvDetails.text = actionDisplayText;
            } else {
                myShortenedText = albumDetails
            }
        }
        binding.tvReadMore.setOnClickListener {
            // Handle click event here, for example, show the full text.
            binding.tvDetails.maxLines = Integer.MAX_VALUE
            binding.tvDetails.text = albumDetails

            binding.tvReadMore.visibility = View.GONE
            binding.tvReadLess.visibility = View.VISIBLE
        }
        binding.tvReadLess.setOnClickListener {
            // Handle click event here, for example, show the full text.
            binding.tvDetails.maxLines = 3
            binding.tvDetails.text = myShortenedText
            binding.tvReadMore.visibility = View.VISIBLE
            binding.tvReadLess.visibility = View.GONE
        }
    }

    private fun createShareLink(redirectUrl:String,albumName:String,artistName:String,imageUrl:String){
        val buo = BranchUniversalObject()
            .setCanonicalIdentifier("albumdetails/$albumName")
            .setTitle("$albumName")
            .setContentDescription("Checkout this cool album")
            .setContentImageUrl(imageUrl)
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
        val lp = LinkProperties()
            .setFeature("sharing")
            .addControlParameter("\$fallback_url", redirectUrl)
            .addControlParameter("album", albumName)
            .addControlParameter("artist",artistName)
            .addControlParameter("sharedScreen","AlbumDetails")
        buo.generateShortUrl(this, lp, Branch.BranchLinkCreateListener {url, error ->
            if (error == null) {
                Log.i("BRANCH SDK", "got my Branch link to share: " + url)
                shareLink(this,url)
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        ActivityNavigator.navigateToDashboard(this)
        finish()
    }
}