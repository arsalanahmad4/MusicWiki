package com.example.musicwiki.genredetails.albums.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.musicwiki.MusicWikiRepository
import com.example.musicwiki.genredetails.albums.model.Album

class AlbumsPagingSource(
    private val musicWikiRepository: MusicWikiRepository, private val tag:String): PagingSource<Int, Album>() {
    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        return try {
            val position = params.key ?: 1
            val response= musicWikiRepository.getTopAlbums(tag,position)
            return LoadResult.Page(
                data = response.body()!!.albums.album,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if ((response.body()?.albums?.attr?.totalPages?.toIntOrNull()
                        ?: 1) <= position
                ) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}