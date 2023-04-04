package com.example.musicwiki

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.musicwiki.api.RetrofitInstance
import com.example.musicwiki.genredetails.albums.adapter.AlbumsPagingSource

class MusicWikiRepository {

    suspend fun getAllGenres() = RetrofitInstance.api.getAllGenres()

    suspend fun getGenreInfo(tag:String) = RetrofitInstance.api.getGenreInfo(tag)
    suspend fun getTopAlbums(tag:String,page:Int) = RetrofitInstance.api.getTopAlbums(tag,page)

    suspend fun getTopArtist(tag:String) = RetrofitInstance.api.getTopArtist(tag)

    suspend fun getTopTracks(tag:String) = RetrofitInstance.api.getTopTracks(tag)

    suspend fun getAlbumDetails(artist:String,album:String) = RetrofitInstance.api.getAlbumDetails(artist,album)

    suspend fun getArtistDetails(artist:String) = RetrofitInstance.api.getArtistDetails(artist)

    fun getPagedIndents(tag: String) = Pager(
        config = PagingConfig(pageSize = 50, maxSize = 100, prefetchDistance = 1),
        pagingSourceFactory = { AlbumsPagingSource(MusicWikiRepository(),tag) }
    ).liveData
}