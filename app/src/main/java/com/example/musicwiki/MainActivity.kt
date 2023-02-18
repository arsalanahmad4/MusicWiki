package com.example.musicwiki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runBlocking {
            MusicWikiRepository().getAllGenres()
        }
    }


}