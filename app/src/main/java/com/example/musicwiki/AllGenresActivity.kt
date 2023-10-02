package com.example.musicwiki

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicwiki.adapter.ElementsAdapter
import com.example.musicwiki.databinding.ActivityAllGenresBinding
import com.example.musicwiki.databinding.ActivityGenreDetailsBinding
import com.example.musicwiki.genredetails.GenreDetailsActivity
import com.example.musicwiki.model.Tag
import com.example.musicwiki.util.Constants
import com.example.musicwiki.util.Resource
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent


class AllGenresActivity : AppCompatActivity() , ElementsAdapter.Callbacks{

    private lateinit var musicWikiViewModel: MusicWikiViewModel

    private var mainList: MutableList<Tag> = ArrayList()
    private val dummyList: MutableList<Tag> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var elementsAdapter: ElementsAdapter? = null

    private var _binding: ActivityAllGenresBinding? = null
    private val binding get() = _binding!!

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        private var doubleBackToExitPressedOnce = false

        override fun handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
            } else {
                doubleBackToExitPressedOnce = true
                Toast.makeText(applicationContext, resources.getString(R.string.back_to_exit), Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicWikiViewModel =
            ViewModelProvider(this, MusicWikiViewModelProviderFactory(application))[MusicWikiViewModel::class.java]
        _binding = ActivityAllGenresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        bindObservers()
        bindView()



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

    // Unregister the onBackPressedCallback in the onDestroy or onDestroyView method
    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

    private fun bindView() {
        _binding?.swipeRefreshLayout?.setOnRefreshListener {
            musicWikiViewModel.getAllGenre()
        }
    }

    override fun onClickLoadMore() {
        elementsAdapter!!.setWithFooter(false) // hide footer

        for (i in 6 until mainList.size) {
            dummyList.add(mainList[i])
        }

        elementsAdapter!!.notifyDataSetChanged()
    }

    override fun onItemClicked(genreName:String) {
        val intent = Intent(this, GenreDetailsActivity::class.java)
        intent.putExtra(Constants.BUNDLE_KEY_GENRE_NAME,genreName)
        startActivity(intent)
        BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEM)
            .setCustomerEventAlias("my_custom_alias")
            .setDescription("Genre Viewed")
            .addCustomDataProperty(Constants.BUNDLE_KEY_GENRE_NAME, genreName)
            .logEvent(applicationContext)
     }

    private fun bindObservers(){
        musicWikiViewModel.allGenreLiveData.observe(this@AllGenresActivity, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { allGenres ->
                        handleShimmer(false)
                        mainList.clear()
                        dummyList.clear()
                        mainList = (allGenres.toptags.tag)
                        for (i in 0..5) {
                            dummyList.add(mainList[i])
                        }
                        elementsAdapter?.notifyDataSetChanged()
                        _binding?.swipeRefreshLayout?.isRefreshing = false
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        handleShimmer(false)
                    }
                }
                is Resource.Loading -> {
                    handleShimmer(true)
                }
            }
        })
    }

    private fun handleShimmer(isShimmering : Boolean){
        if(isShimmering){
            _binding?.shimmerFrameLayout?.visibility = View.VISIBLE
            _binding?.recyclerView?.visibility = View.GONE
        }else{
            _binding?.shimmerFrameLayout?.visibility = View.GONE
            _binding?.recyclerView?.visibility = View.VISIBLE
        }
    }


}