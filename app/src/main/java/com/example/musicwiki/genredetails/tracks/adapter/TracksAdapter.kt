package com.example.musicwiki.genredetails.tracks.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicwiki.R
import com.example.musicwiki.genredetails.artists.model.Artist
import com.example.musicwiki.genredetails.tracks.model.Track


class TracksAdapter(private val mData: List<Track>) : RecyclerView.Adapter<TracksAdapter.ArtistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.albums_list_item, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val data = mData[position]
        holder.bindData(data)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView
        private val imageView: ImageView

        init {
            titleTextView = itemView.findViewById<TextView>(R.id.title_text_view)
            imageView = itemView.findViewById<ImageView>(R.id.background_image_view)
        }

        fun bindData(data: Track) {
            titleTextView.text = data.name
            Glide.with(itemView.context)
                .load(data.image[2].text)
                .centerCrop()
                .into(imageView)
        }
    }
}