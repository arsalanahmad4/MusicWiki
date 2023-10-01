package com.example.musicwiki

import android.app.Application
import android.content.Context
import io.branch.referral.Branch




class MusicWikiApplication:Application(){

    companion object {
        private lateinit var instance: MusicWikiApplication

        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Branch logging for debugging
        Branch.enableLogging()
        // Branch object initialization
        Branch.getAutoInstance(this)
    }
}