package com.example.musicwiki.genredetails.albums.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicwiki.R

class LoadAdapter : LoadStateAdapter<LoadAdapter.LoaderViewHolder>() {

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.paging_loader_item,parent,false)
        return LoaderViewHolder(view)
    }

    class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
        fun bind(loadState: LoadState){
            progressBar.isVisible = loadState is LoadState.Loading
        }

    }
}