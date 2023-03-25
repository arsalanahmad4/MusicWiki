package com.example.musicwiki.api

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.musicwiki.MusicWikiApplication

object ChuckerHandler {
    fun init(): ChuckerInterceptor? {
        val chuckerCollector = ChuckerCollector(
            context = MusicWikiApplication.getAppContext(),
            // Toggles visibility of the notification
            showNotification = true,
            // Allows to customize the retention period of collected data
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        // Create the Interceptor

        return ChuckerInterceptor.Builder(MusicWikiApplication.getAppContext())
            .collector(chuckerCollector)
            .maxContentLength(250_000L)
            .alwaysReadResponseBody(true)
            .build()
        return null
    }

}