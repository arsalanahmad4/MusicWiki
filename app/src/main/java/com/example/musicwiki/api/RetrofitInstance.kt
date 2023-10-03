package com.example.musicwiki.api

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.musicwiki.MusicWikiApplication
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

        private fun getOkHttpClient(): OkHttpClient {
            val okHttpClient = OkHttpClient.Builder()
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
            okHttpClient.addInterceptor(ChuckerHandler.init()!!)

            return okHttpClient.build()
        }

        val api: LastFMApi by lazy {
            retrofit.create(LastFMApi::class.java)
        }


        private val branchRetrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://api2.branch.io")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getBranchOkHttpClient())
                .build()
        }

        private fun getBranchOkHttpClient(): OkHttpClient {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val url = chain
                        .request()
                        .url
                        .newBuilder()
                        .build()
                    chain.proceed(chain.request().newBuilder().addHeader("Content-Type","application/json").url(url).build())
                }
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            okHttpClient.addInterceptor(ChuckerHandler.init()!!)

            return okHttpClient.build()
        }

        val branchApi: LastFMApi by lazy {
            branchRetrofit.create(LastFMApi::class.java)
        }

    }


}

