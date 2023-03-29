package com.example.musicwiki.util

import android.os.Bundle
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class CustomInfiniteScroller : RecyclerView.OnScrollListener, CustomScrollerCallBack {
    var startPage = 0

    constructor()

    constructor(startPageIndex: Int) {
        this.startPage = startPageIndex
    }

    constructor(startPageIndex: Int, dataFetchSize: Int, savedInstanceState: Bundle?) {
        this.startPage = startPageIndex
        DATA_FETCH_SIZE = dataFetchSize
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt("currentPage")
        }
    }

    constructor(startPageIndex: Int, dataFetchSize: Int) {
        this.startPage = startPageIndex
        DATA_FETCH_SIZE = dataFetchSize
    }

    companion object {
        var INITIAL_PAGE_INDEX = 0
        var DATA_FETCH_SIZE = 15
    }

    private var isRecyclerViewScrolling = false

    // The current offset index of data you have loaded
    private var currentPage = startPage

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isRecyclerViewScrolling = true
        }
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0 && isRecyclerViewScrolling) {
            val firstVisibleItem =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val totalItemCount = (recyclerView.layoutManager as LinearLayoutManager).itemCount
            val visibleItemCount = (recyclerView.layoutManager as LinearLayoutManager).childCount
            if (loading) {
                if (visibleItemCount + firstVisibleItem >= totalItemCount && totalItemCount > currentPage * DATA_FETCH_SIZE + 1) {
                    loading = false
                    isRecyclerViewScrolling = false
                    onLoadMoreData(page = ++currentPage, totalItemCount)
                    loading = true
                }
            }
        }

    }

    override fun onReset() {
        currentPage = INITIAL_PAGE_INDEX
    }

    // Defines the process for actually loading more data based on page
    // Returns true if more data is being loaded; returns false if there is no more data to load.
    abstract fun onLoadMoreData(page: Int, totalItemsCount: Int): Boolean

}