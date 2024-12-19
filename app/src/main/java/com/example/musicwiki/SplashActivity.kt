package com.example.musicwiki

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.musicwiki.model.BranchResponseData
import com.example.musicwiki.util.ActivityNavigator
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchContentSchema
import io.branch.referral.util.BranchEvent
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.CurrencyType
import io.branch.referral.util.ProductCategory


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
//        Branch.sessionBuilder(this).withCallback(branchReferralInitListener)
//            .withData(if (intent != null) intent.data else null).init()
        initBranch()
    }

    private fun initBranch(){
        Branch.sessionBuilder(this).withCallback { branchUniversalObject, linkProperties, error ->
            if (error != null) {
                Log.e("BranchSDK_Tester", "branch init failed. Caused by -" + error.message)
            } else {

            }
        }.withData(this.intent.data).init()
    }

    private val branchReferralInitListener =
        Branch.BranchReferralInitListener { linkProperties, error ->
            if (error == null) {
                Log.i("BranchDeepLink", linkProperties.toString())
                try {
                    val gson = Gson()
                    val parser = JsonParser()
                    val json = parser.parse(linkProperties.toString())
//                    val data = Branch.getAutoInstance(this).firstReferringParamsSync
//                    Log.e("TestFirstInstall", data.toString())
                    val branchDataModel = gson.fromJson(json, BranchResponseData::class.java)
                    navigateToLandingScreen(this,branchDataModel)
                    finishAffinity()
                } catch (e: Exception) {
                    e.printStackTrace()
                    navigateToLandingScreen(this,null)
                    finishAffinity()
                }
            }
        }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initBranch()
        //setIntent(intent)
        //Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit()

    }

    private fun navigateToLandingScreen(context: Context,model:BranchResponseData?){
        if(model != null){
            if(model.sharedScreen == "AlbumDetails" && !model.album.isNullOrEmpty() && !model.artist.isNullOrEmpty()){
                ActivityNavigator.navigateToAlbumDetailsScreen(context, model.album!!, model.artist!!)
            }else if(model.sharedScreen == "ArtistDetails" && !model.artist.isNullOrEmpty()){
                ActivityNavigator.navigateToArtistDetailsScreen(context,model.artist!!)
            } else{
                ActivityNavigator.navigateToDashboard(context)
            }
            //triggerPurchase()
        }
    }

    private fun triggerPurchase(){
        val buo = BranchUniversalObject()
            .setCanonicalIdentifier("myprod/1234")
            .setCanonicalUrl("https://test_canonical_url")
            .setTitle("test_title")
            .setContentMetadata(
                ContentMetadata()
                    .addImageCaptions("image_caption_1", "image_caption2", "image_caption3")
                    .setAddress("Street_Name", "test city", "test_state", "test_country", "test_postal_code")
                    .setRating(5.2, 6.0, 5)
                    .setLocation(-151.67, -124.0)
                    .setPrice(100.0, CurrencyType.USD)
                    .setProductBrand("test_prod_brand")
                    .setProductCategory(ProductCategory.APPAREL_AND_ACCESSORIES)
                    .setProductName("test_prod_name")
                    .setProductCondition(ContentMetadata.CONDITION.EXCELLENT)
                    .setProductVariant("test_prod_variant")
                    .setQuantity(1.5)
                    .setSku("test_sku")
                    .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT)
            )
            .addKeyWord("keyword1")
            .addKeyWord("keyword2")


//  Create an event
        BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
            .setAffiliation("test_affiliation")
            .setCustomerEventAlias("my_custom_alias")
            .setCoupon("Coupon Code")
            .setCurrency(CurrencyType.USD)
            .setDescription("Customer added item to cart")
            .setShipping(0.0)
            .setTax(9.75)
            .setRevenue(100.0)
            .setSearchQuery("Test Search query")
            .addCustomDataProperty("Custom_Event_Property_Key1", "testValue1")
            .addCustomDataProperty("Custom_Event_Property_Key2", "testValue2")
            .addContentItems(buo) // Add a BranchUniversalObject to the event (cannot be empty)
            .logEvent(applicationContext)
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
//                Log.e(TAG, "PERMISSION_GRANTED")
                // FCM SDK (and your app) can post notifications.
            } else {
//                Log.e(TAG, "NO_PERMISSION")
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
//            Toast.makeText(
//                this, "${getString(R.string.app_name)} can't post notifications without Notification permission",
//                Toast.LENGTH_LONG
//            ).show()


        }
    }
}