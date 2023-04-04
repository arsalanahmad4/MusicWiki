package com.example.musicwiki.genredetails.albums.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicwiki.databinding.AlbumsListItemBinding
import com.example.musicwiki.genredetails.albums.model.Album

class AlbumsPagingAdapter(private val interaction: Interaction? = null): PagingDataAdapter<Album, AlbumsPagingAdapter.AlbumsViewHolder>(
    COMPARATOR
) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return (oldItem.name == newItem.name && oldItem.url == newItem.url)
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        (holder as AlbumsPagingAdapter.AlbumsViewHolder).bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumsViewHolder {
        val itemBinding = AlbumsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumsViewHolder(itemBinding,interaction)
    }

    class AlbumsViewHolder(private val itemBinding: AlbumsListItemBinding,
                           private val interaction: Interaction?) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: Album?) = with(itemBinding) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemBinding.titleTextView.text = item?.name
            Glide.with(itemView.context)
                .load(item?.image?.get(2)?.text)
                .centerCrop()
                .into(itemBinding.backgroundImageView)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Album?)
    }
}