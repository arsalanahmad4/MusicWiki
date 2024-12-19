package com.example.musicwiki

import android.app.Application
import android.content.Context
import io.branch.referral.Branch
import io.branch.referral.BranchLogger

//import io.branch.referral.BranchLogger


class MusicWikiApplication:Application(){

    companion object {
        private lateinit var instance: MusicWikiApplication

        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Branch logging for debugging
        //Branch.enableLogging()
        Branch.enableLogging(BranchLogger.BranchLogLevel.VERBOSE)
        Branch.getAutoInstance(this)
        //Branch.expectDelayedSessionInitialization(true)
        // Branch object initialization
        //Branch.getAutoInstance(this,"key_test_bCcAkJPCucFyotVAP3A1BfbfCukrFp8q")
        //Branch.getAutoInstance(this).disableTracking(false)
    }
}