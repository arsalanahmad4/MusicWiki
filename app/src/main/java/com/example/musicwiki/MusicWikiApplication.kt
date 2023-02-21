package com.example.musicwiki

import android.app.Application
import android.content.Context

class MusicWikiApplication:Application(){

    companion object{
        var appContext: Context? = null
    }
}