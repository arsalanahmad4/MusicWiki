package com.example.musicwiki.api

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {
    companion object {

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("http://ws.audioscrobbler.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build()
        }

        fun getOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val url = chain
                        .request()
                        .url
                        .newBuilder()
                        .addQueryParameter("api_key", "f432e7a47de731dfc3b75d97b235746a")
                        .addQueryParameter("format","json")
                        .build()
                    chain.proceed(chain.request().newBuilder().url(url).build())
                }
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        }

        val api: LastFMApi by lazy {
            retrofit.create(LastFMApi::class.java)
        }

    }


}

