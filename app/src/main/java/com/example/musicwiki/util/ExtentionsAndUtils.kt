package com.example.musicwiki.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.example.musicwiki.R
import java.util.*


@SuppressLint("ObsoleteSdkInt")
fun hasInternetConnection(connectivityManager: ConnectivityManager) : Boolean{
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.activeNetworkInfo?.run {
            return when(type) {
                ConnectivityManager.TYPE_WIFI -> true
                ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_ETHERNET -> true
                else -> false
            }
        }
    }
    return false
}

fun openUrlInCustomTabIntent(url:String,activity: Activity){
    val customIntent = CustomTabsIntent.Builder()
    customIntent.setToolbarColor(
        ContextCompat.getColor(
            activity,
            R.color.purple_theme_color
        )
    )
    openCustomTab(activity,customIntent.build(), Uri.parse(url))
}
fun openCustomTab(activity: Activity, customTabsIntent: CustomTabsIntent, uri: Uri) {
    // package name is the default package
    // for our custom chrome tab
    val packageName = "com.android.chrome"
    if (packageName != null) {

        // we are checking if the package name is not null
        // if package name is not null then we are calling
        // that custom chrome tab with intent by passing its
        // package name.
        customTabsIntent.intent.setPackage(packageName)

        // in that custom tab intent we are passing
        // our url which we have to browse.
        customTabsIntent.launchUrl(activity, uri)
    } else {
        // if the custom tabs fails to load then we are simply
        // redirecting our user to users device default browser.
        activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}

fun shareLink(context: Context, shareUrl: String?) {
    Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareUrl)
        Intent.createChooser(this, "Share via").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }.also {
            try {
                context.startActivity(it)
            } catch (e: java.lang.Exception) {

            }
        }
    }
}