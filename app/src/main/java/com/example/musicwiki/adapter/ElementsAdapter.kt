package com.example.musicwiki.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicwiki.R
import com.example.musicwiki.model.Tag

class ElementsAdapter(private val mFeedList: List<Tag>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface Callbacks {
        fun onClickLoadMore()
        fun onItemClicked(genreName:String)
    }

    private var mCallbacks: Callbacks? = null
    private var mWithHeader = false
    private var mWithFooter = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var itemView: View? = null
        return if (viewType == TYPE_FOOTER) {
            itemView = View.inflate(parent.context, R.layout.row_loadmore, null)
            LoadMoreViewHolder(itemView)
        } else {
            itemView = View.inflate(parent.context, R.layout.row_element, null)
            ElementsViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LoadMoreViewHolder) {
            holder.itemView.setOnClickListener { if (mCallbacks != null) mCallbacks!!.onClickLoadMore() }
        } else {
            val elementsViewHolder = holder as ElementsViewHolder?
            val elements = mFeedList[position]
            elementsViewHolder?.itemView?.setOnClickListener {
                if (mCallbacks != null) mCallbacks!!.onItemClicked(elements.name)
            }
            elementsViewHolder!!.name.text = elements.name
        }
    }

    override fun getItemCount(): Int {
        var itemCount = mFeedList.size
        if (mWithHeader) itemCount++
        if (mWithFooter) itemCount++
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        if (mWithHeader && isPositionHeader(position)) return TYPE_HEADER
        return if (mWithFooter && isPositionFooter(position)) TYPE_FOOTER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0 && mWithHeader
    }

    fun isPositionFooter(position: Int): Boolean {
        return position == itemCount - 1 && mWithFooter
    }

    fun setWithHeader(value: Boolean) {
        mWithHeader = value
    }

    fun setWithFooter(value: Boolean) {
        mWithFooter = value
    }

    fun setCallback(callbacks: Callbacks?) {
        mCallbacks = callbacks
    }

    inner class ElementsViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        val name: TextView

        init {
            name = itemView!!.findViewById<View>(R.id.name) as TextView
        }
    }

    inner class LoadMoreViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    )

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }
}