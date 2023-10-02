package com.example.musicwiki.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.musicwiki.AllGenresActivity
import com.example.musicwiki.genredetails.albums.albumdetails.AlbumDetailsActivity
import com.example.musicwiki.genredetails.artists.artistdetails.ArtistDetailsActivity

object ActivityNavigator {

    fun navigateToDashboard(context: Context){
        val dashboardScreen = Intent(context, AllGenresActivity::class.java)
        val bundle = Bundle()
        bundle.apply {

        }
        dashboardScreen.putExtras(bundle)
        context.startActivity(dashboardScreen)
    }

    fun navigateToAlbumDetailsScreen(context: Context, album:String,artist:String){
        val artistDetailsIntent = Intent(context, AlbumDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.apply {
            putString("album", album)
            putString("artist",artist)
        }
        artistDetailsIntent.putExtras(bundle)
        context.startActivity(artistDetailsIntent)
    }

    fun navigateToArtistDetailsScreen(context:Context,artist:String){
        val artistDetailsIntent = Intent(context, ArtistDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.apply {
            putString("artist", artist)
        }
        artistDetailsIntent.putExtras(bundle)
        context.startActivity(artistDetailsIntent)
    }
}