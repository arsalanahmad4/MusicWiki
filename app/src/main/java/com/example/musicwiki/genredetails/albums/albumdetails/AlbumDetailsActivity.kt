package com.example.musicwiki.genredetails.albums.albumdetails

import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicwiki.R
import com.example.musicwiki.databinding.ActivityAlbumDetailsBinding
import com.example.musicwiki.genredetails.GenreDetailsViewModel
import com.example.musicwiki.genredetails.GenreViewModelProviderFactory
import com.example.musicwiki.genredetails.albums.albumdetails.model.AlbumDetailsResponse
import com.example.musicwiki.genredetails.albums.albumdetails.model.Tag
import com.example.musicwiki.util.ActivityNavigator
import com.example.musicwiki.util.Resource
import com.example.musicwiki.util.gone
import com.example.musicwiki.util.shareLink
import com.example.musicwiki.util.visible
import com.google.android.material.chip.Chip
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchContentSchema
import io.branch.referral.util.BranchEvent
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.CurrencyType
import io.branch.referral.util.LinkProperties
import io.branch.referral.util.ProductCategory


class AlbumDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: GenreDetailsViewModel

    private lateinit var binding: ActivityAlbumDetailsBinding

    private var artist: String? = null
    private var album: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(
                this,
                GenreViewModelProviderFactory(application)
            )[GenreDetailsViewModel::class.java]
        binding = ActivityAlbumDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        artist = intent.extras?.getString("artist")
        album = intent.extras?.getString("album")

        var deviceID = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        Log.e("DeviceId", deviceID)

        binding.toolbar.toolbarBack.setOnClickListener {
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
                    binding.rlLoader.gone()
                    response.data?.let { albumDetails ->
                        setApiResponseData(albumDetails)
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
        if (!artist.isNullOrEmpty() && !album.isNullOrEmpty()) {
            viewModel.getAlbumDetails(artist!!, album!!)
        }
    }

    private fun setApiResponseData(albumDetails: AlbumDetailsResponse) {
        binding.tvTotalListenersCount.text = albumDetails.album?.listeners ?: ""
        binding.tvPlayCount.text = albumDetails.album?.playcount ?: ""
        setDetailsText(albumDetails.album?.wiki?.summary)
        albumDetails.album?.tags?.tag?.let { addChipsFromList(it) }
        binding.tvVisitWebsite.setOnClickListener {
            //openUrlInCustomTabIntent("https://f6mb.app.link/?\$deeplink_path=shopback%3A%2F%2Fcampaign%3Furl%3Dhttps%3A%2F%2Fwww.shopback.co.id%2Fredirect%2Falink%2F1019058&~feature=CRM&~channel=ps&~campaign=%5BID%20Online%5D_mx_bd_manual_ps_20241119_1200_LVA1%2BLVA2%2BLVA3-Android_Kredivo"?: "", this)
            val webView = WebView(this)
            val url = "https://f6mb.app.link/?\$deeplink_path=shopback%3A%2F%2Fcampaign%3Furl%3Dhttps%3A%2F%2Fwww.shopback.co.id%2Fredirect%2Falink%2F1019058&~feature=CRM&~channel=ps&~campaign=%5BID%20Online%5D_mx_bd_manual_ps_20241119_1200_LVA1%2BLVA2%2BLVA3-Android_Kredivo"
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    // Log the URL that is about to be loaded
                    Log.d("WebView", "Loading URL: $url")
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }

            // Load the URL into the WebView
            webView.loadUrl(url)
            trackCommerceEvent(albumDetails.album?.url ?: "",albumDetails.album?.name ?: "",albumDetails.album?.artist ?: "")
        }

        val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip: String = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        binding.toolbar.llLinkShare.setOnClickListener {
//            createShareLink(
//                albumDetails.album?.url ?: "",
//                albumDetails.album?.name ?: "",
//                albumDetails.album?.artist ?: "",
//                albumDetails.album?.image?.get(0)?.text ?: "",
//                ip,
//                deviceId
//            )
        }
    }

    private fun addChipsFromList(tag: List<Tag>) {
        binding.llSelectionTabs.removeAllViews()
        for ((index, slot) in tag.withIndex()) {
            if (slot != null) {
                val chip = Chip(this, null, R.attr.CustomChipChoiceStyle)
                chip.text = slot.name
                chip.isCheckable = true

                // Set a click listener for the close icon
                binding.llSelectionTabs.addView(chip)
            }
        }
    }


    private fun setDetailsText(albumDetails: String?) {
        if (!albumDetails.isNullOrEmpty()) {
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
        } else {
            binding.tvDetails.gone()
            binding.tvReadLess.gone()
            binding.tvReadMore.gone()
            binding.tvAbout.gone()
        }
    }

    private fun createShareLink(
        redirectUrl: String,
        albumName: String,
        artistName: String,
        imageUrl: String,
        ipAddress: String,
        deviceId: String
    ) {
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
            .addControlParameter("artist", artistName)
            .addControlParameter("sharedScreen", "AlbumDetails")
        buo.generateShortUrl(this, lp, Branch.BranchLinkCreateListener { url, error ->
            if (error == null) {
                Log.i("BRANCH SDK", "got my Branch link to share: " + url)
                viewModel.postCustomEvent(
                    branchKey = getString(R.string.branch_key_live),
                    eventName = "AlbumLink",
                    eventAlias = "album_alias",
                    link = url,
                    artist = artistName,
                    album = albumName,
                    eventDescription = "Album Shared",
                    eventSearchQuery = "album name",
                    androidId = deviceId,
                    localIp = ipAddress
                )
                shareLink(this, url)
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        ActivityNavigator.navigateToDashboard(this)
        finish()
    }

    private fun trackLinkCreation(link: String, album: String, artist: String) {
        BranchEvent("AlbumLink")
            .setCustomerEventAlias("album_alias")
            .setDescription("Album Shared")
            .setSearchQuery("album name")
            .addCustomDataProperty("CreatedLink", link)
            .addCustomDataProperty("album", album)
            .addCustomDataProperty("artist", artist)
            .logEvent(applicationContext)
    }

    private fun trackCommerceEvent(url:String,albumName:String,artistName: String){
        val buo = BranchUniversalObject()
            .setCanonicalIdentifier("musicalbum/${albumName}")
            .setCanonicalUrl(url)
            .setTitle(albumName)
            .setContentMetadata(
                ContentMetadata()
                    .addCustomMetadata("albumName", albumName)
                    .addCustomMetadata("artistName", artistName)
                    .setRating(5.2, 6.0, 5)
                    .setPrice(10.0, CurrencyType.USD)
                    .setProductBrand("MusicWiki")
                    .setProductCategory(ProductCategory.SOFTWARE)
                    .setProductName("Music Wiki WebSite")
                    .setProductCondition(ContentMetadata.CONDITION.EXCELLENT)
                    .setProductVariant("test_prod_variant")
                    .setQuantity(1.5)
                    .setSku("test_sku")
                    .setContentSchema(BranchContentSchema.COMMERCE_OTHER)
            )
            .addKeyWord(albumName)
            .addKeyWord(artistName)


//  Do not add an empty branchUniversalObject to the BranchEvent
        BranchEvent(BRANCH_STANDARD_EVENT.VIEW_AD)
            .setAffiliation("test_affiliation")
            .setCustomerEventAlias("my_custom_alias")
            .setCoupon("Coupon Code")
            .setCurrency(CurrencyType.USD)
            .setDescription("Customer visited website")
            .setShipping(0.0)
            .setRevenue(1.5)
            .setSearchQuery("Test Search query")
            .addCustomDataProperty("EVENT_PROPERTY_ARTIST", artistName)
            .addCustomDataProperty("EVENT_PROPERTY_ALBUM", albumName)
            .addContentItems(buo)
            .logEvent(applicationContext)
    }
}