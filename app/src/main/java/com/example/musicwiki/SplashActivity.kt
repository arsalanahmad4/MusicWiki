package com.example.musicwiki

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musicwiki.model.BranchResponseData
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.branch.referral.Branch
import io.branch.referral.BranchError
import org.json.JSONObject


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            navigateToLandingScreen(this)
            finish()
        }, 2000L)
    }

    override fun onStart() {
        super.onStart()
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener)
            .withData(if (intent != null) intent.data else null).init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit()
    }

    private val branchReferralInitListener =
        Branch.BranchReferralInitListener { linkProperties, error ->
            if (error == null) {
                Log.i("BranchDeepLink", linkProperties.toString())
                try {
                    val gson = Gson()
                    val parser = JsonParser()
                    val json = parser.parse(linkProperties.toString())
                    val shareScreenDataModel = gson.fromJson(json, BranchResponseData::class.java)
                    Log.e("BranchDeepLink",shareScreenDataModel.genere?:"")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    private fun navigateToLandingScreen(context: Context){
        val artistDetailsIntent = Intent(context, AllGenresActivity::class.java)
        val bundle = Bundle()
        bundle.apply {

        }
        artistDetailsIntent.putExtras(bundle)
        context.startActivity(artistDetailsIntent)
    }
}