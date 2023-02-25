package com.example.musicwiki

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicwiki.adapter.ElementsAdapter
import com.example.musicwiki.model.Tag
import com.example.musicwiki.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() , ElementsAdapter.Callbacks{

    private lateinit var musicWikiViewModel: MusicWikiViewModel

    private var mainList: MutableList<Tag> = ArrayList()
    private val dummyList: MutableList<Tag> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var elementsAdapter: ElementsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicWikiViewModel =
            ViewModelProvider(this, MusicWikiViewModelProviderFactory(application))[MusicWikiViewModel::class.java]
        setContentView(R.layout.activity_main)

        bindObservers()



        musicWikiViewModel.getAllGenre()

        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView

        val columns = 2
        val gridLayoutManager = GridLayoutManager(this, columns)

//        recyclerView.addItemDecoration(GridDividerDecoration(this))
        recyclerView?.layoutManager = gridLayoutManager

        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (elementsAdapter!!.isPositionFooter(position)) gridLayoutManager.spanCount else 1
            }
        }


        elementsAdapter = ElementsAdapter(dummyList)
        elementsAdapter!!.setCallback(this)
        elementsAdapter!!.setWithFooter(true) //enabling footer to show

        recyclerView?.adapter = elementsAdapter
    }

    override fun onClickLoadMore() {
        elementsAdapter!!.setWithFooter(false) // hide footer

        for (i in 6 until mainList.size) {
            dummyList.add(mainList[i])
        }

        elementsAdapter!!.notifyDataSetChanged()
    }

    override fun onItemClicked() {
     }

    private fun bindObservers(){
        musicWikiViewModel.allGenreLiveData.observe(this@MainActivity, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { allGenres ->
                        mainList = (allGenres.toptags.tag)
                        for (i in 0..5) {
                            dummyList.add(mainList[i])
                        }
                        elementsAdapter?.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->

                    }
                }
                is Resource.Loading -> {

                }
            }
        })
    }


}