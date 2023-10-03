package com.example.musicwiki.genredetails.artists.artistdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicwiki.util.ActivityNavigator
import com.example.musicwiki.R
import com.example.musicwiki.databinding.ActivityArtistDetailsBinding
import com.example.musicwiki.genredetails.GenreDetailsViewModel
import com.example.musicwiki.genredetails.GenreViewModelProviderFactory
import com.example.musicwiki.genredetails.artists.artistdetails.model.ArtistDetailsResponse
import com.example.musicwiki.genredetails.artists.artistdetails.model.Tag
import com.example.musicwiki.util.Resource
import com.example.musicwiki.util.gone
import com.example.musicwiki.util.openUrlInCustomTabIntent
import com.example.musicwiki.util.shareLink
import com.example.musicwiki.util.visible
import com.google.android.material.chip.Chip
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.BranchEvent
import io.branch.referral.util.LinkProperties

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
                    binding.rlLoader.gone()
                    response.data?.let { artistDetails ->
                        setApiResponseData(artistDetails)
                    }
                }
                is Resource.Error -> {
                    binding.rlLoader.gone()
                    response.message?.let { message ->

                    }
                }
                is Resource.Loading -> {
                    binding.rlLoader.visible()
                }
            }
        })
    }

    private fun bindView() {
        artist?.let { viewModel.getArtistDetails(it) }
    }

    private fun setApiResponseData(artistDetails: ArtistDetailsResponse) {
        binding.tvTotalListenersCount.text = artistDetails.artist?.stats?.listeners?:""
        binding.tvPlayCount.text = artistDetails.artist?.stats?.playcount?:""
        setDetailsText(artistDetails.artist?.bio?.content)
        artistDetails.artist?.tags?.tag?.let { addChipsFromList(it) }
        binding.tvVisitWebsite.setOnClickListener {
            openUrlInCustomTabIntent(artistDetails?.artist?.url?:"",this)
        }
        binding.toolbar.llLinkShare.setOnClickListener{
            createShareLink(artistDetails.artist?.url?:"",artistDetails.artist?.name?:"",artistDetails.artist?.image?.get(0)?.text?:"")
        }
    }

    private fun setDetailsText(artistDetails: String?) {
        if(!artistDetails.isNullOrEmpty()){
            var myShortenedText = ""
            binding.tvDetails.text = artistDetails;
            binding.tvDetails.post {
                // Past the maximum number of lines we want to display.
                if (binding.tvDetails.lineCount > 3) {
                    val lastCharShown = binding.tvDetails.layout.getLineVisibleEnd(3 - 1);

                    binding.tvDetails.maxLines = 3;
                    val actionDisplayText = artistDetails.substring(0, lastCharShown) + "   "

                    myShortenedText = actionDisplayText
                    // Set the truncated text to the TextView
                    binding.tvReadMore.visibility = View.VISIBLE
                    binding.tvDetails.text = actionDisplayText;
                } else {
                    myShortenedText = artistDetails
                }
            }
            binding.tvReadMore.setOnClickListener {
                // Handle click event here, for example, show the full text.
                binding.tvDetails.maxLines = Integer.MAX_VALUE
                binding.tvDetails.text = artistDetails

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
        }else{
            binding.tvDetails.gone()
            binding.tvReadLess.gone()
            binding.tvReadMore.gone()
            binding.tvAbout.gone()
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

    private fun createShareLink(redirectUrl:String,artistName:String,imageUrl:String){
        val buo = BranchUniversalObject()
            .setCanonicalIdentifier("artistDetails/$artistName")
            .setTitle("$artistName")
            .setContentDescription("Checkout this amazing artist")
            .setContentImageUrl(imageUrl)
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
        val lp = LinkProperties()
            .setFeature("sharing")
            .addControlParameter("\$fallback_url", redirectUrl)
            .addControlParameter("artist",artistName)
            .addControlParameter("sharedScreen","ArtistDetails")
        buo.generateShortUrl(this, lp, Branch.BranchLinkCreateListener { url, error ->
            if (error == null) {
                Log.i("BRANCH SDK", "got my Branch link to share: " + url)
                trackLinkCreation(url,artistName)
                shareLink(this,url)
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        ActivityNavigator.navigateToDashboard(this)
        finish()
    }

    private fun trackLinkCreation(link:String,artist:String){
        BranchEvent("ArtistLink")
            .setCustomerEventAlias("artist_alias")
            .setDescription("Artist Shared")
            .setSearchQuery("artist name")
            .addCustomDataProperty("CreatedLink", link)
            .addCustomDataProperty("artist",artist)
            .logEvent(applicationContext)
    }
}