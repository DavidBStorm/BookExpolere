package com.fara.bookexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fara.bookexplorer.domain.model.Doc
import com.fara.bookexpolorer.databinding.BookItemBinding


class BookAdapter(private val onClick: (Doc) -> Unit) : ListAdapter<Doc, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    class BookDiffCallback : DiffUtil.ItemCallback<Doc>() {
        override fun areItemsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            // Check if items represent the same book, by comparing their unique ID (or any other unique property)
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            // Check if the contents of the book are the same
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class BookViewHolder(private val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Doc) {
            binding.bookTitle.text = book.title
            // You can bind other views like book author, etc.

            binding.root.setOnClickListener {
                onClick(book) // Trigger the click listener
            }
        }
    }


    fun submitBookList(books: List<Doc>) {
        submitList(books)
    }
}

