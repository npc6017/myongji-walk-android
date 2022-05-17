package com.example.myoungji_walk_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myoungji_walk_android.Model.BookMark
import com.example.myoungji_walk_android.databinding.ItemBookmarkBinding

class BookMarkAdapter(): ListAdapter<BookMark, BookMarkAdapter.ItemViewHolder>(diffUtil) {

    inner class ItemViewHolder(private val binding: ItemBookmarkBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(bookMark: BookMark){
            binding.bookMarkTextView.text = bookMark.keyword
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BookMark>() {
            override fun areItemsTheSame(oldItem: BookMark, newItem: BookMark): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BookMark, newItem: BookMark): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}