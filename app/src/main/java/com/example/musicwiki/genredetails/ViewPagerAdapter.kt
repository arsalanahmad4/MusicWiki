package com.example.musicwiki.genredetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicwiki.util.Constants


class ViewPagerAdapter(private val tag:String,private val fragmentList:ArrayList<Fragment>, fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle){
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = fragmentList[position]
        val args = Bundle()
        args.putString(Constants.BUNDLE_KEY_GENRE_NAME,tag)
        fragment.arguments = args
        return fragment
    }


}