package com.fara.bookexplorer.ui.fragments.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fara.bookexplorer.domain.model.Doc
import com.fara.bookexplorer.ui.base.BaseFragment
import com.fara.bookexpolorer.R
import com.fara.bookexpolorer.databinding.FragmentDetailsBinding


class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {
    private var book: Doc? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        arguments?.let {
            book = it.getParcelable(ARG_BOOK)
        }
        Log.e("eee", "onCreateView: ${book?.title}")
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding {
        return FragmentDetailsBinding.inflate(inflater, container, false)
    }


    companion object {
        private const val ARG_BOOK = "arg_book"

        fun newInstance(book: Doc): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle().apply {
                putParcelable(ARG_BOOK, book) // Assuming `Doc` implements `Parcelable`
            }
            fragment.arguments = args
            return fragment
        }
    }
}