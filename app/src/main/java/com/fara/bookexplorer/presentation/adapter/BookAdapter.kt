package com.fara.bookexplorer.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fara.bookexplorer.domain.model.Doc
import com.fara.bookexpolorer.databinding.BookItemBinding



class BookAdapter(private val onClick: (Doc) -> Unit) : PagingDataAdapter<Doc, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    class BookDiffCallback : DiffUtil.ItemCallback<Doc>() {
        override fun areItemsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return oldItem.key == newItem.key // Unique book identifier
        }

        override fun areContentsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return oldItem == newItem // Compare content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        book?.let { holder.bind(it) } // Handle null check
    }

    inner class BookViewHolder(private val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Doc) {
            binding.book = book
            binding.root.setOnClickListener { onClick(book) }
        }
    }


}
