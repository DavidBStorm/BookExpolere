package com.fara.bookexplorer.ui.fragments.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fara.bookexplorer.ui.adapter.BookAdapter
import com.fara.bookexplorer.ui.base.BaseFragment
import com.fara.bookexplorer.ui.state.MainUIState
import com.fara.bookexplorer.viewModel.main.MainViewModel
import com.fara.bookexpolorer.R
import com.fara.bookexpolorer.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private lateinit var viewModel: MainViewModel
    private lateinit var bookAdapter: BookAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        // Initialize Adapter
        bookAdapter = BookAdapter { book ->
            // Handle book click, e.g., navigate to DetailFragment
        }

        // Setup RecyclerView
        binding.recyclerView.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!viewModel.isLoading && !viewModel.isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                            // User has scrolled to the bottom of the list
                            Log.e("TAG", "onScrolled: load more" )
                            viewModel.loadMoreBooks()
                        }
                    }
                }
            })
        }

        // Observe ViewModel state
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is MainUIState.Loading -> {
                        Log.e("eee", "onViewCreated: on progress")
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainUIState.Success -> {
                        val books = state.books
                        bookAdapter.submitBookList(books?.docs.orEmpty())
                        Log.e("eee", "onViewCreated: on success ${books?.numFound}")
                    }
                    is MainUIState.Error -> {
                        // Show error message
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    MainUIState.Idle -> {
                        Log.e("eee", "onViewCreated: on idle")
                        // Initial state, no action required
                    }
                }
            }
        }

        // Setup SearchView
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.onQueryChanged(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.onQueryChanged(it)
                }
                return true
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
