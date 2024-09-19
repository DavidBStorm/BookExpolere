package com.fara.bookexplorer.ui.fragments.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fara.bookexplorer.ui.adapter.BookAdapter
import com.fara.bookexplorer.ui.adapter.BookLoadStateAdapter
import com.fara.bookexplorer.ui.base.BaseFragment
import com.fara.bookexplorer.ui.state.MainIntent
import com.fara.bookexplorer.ui.state.MainUIState
import com.fara.bookexplorer.viewModel.main.MainViewModel
import com.fara.bookexpolorer.R
import com.fara.bookexpolorer.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private lateinit var viewModel: MainViewModel
    private lateinit var bookAdapter: BookAdapter
    private lateinit var progressDialog: AlertDialog


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

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookAdapter.withLoadStateFooter(
                footer = BookLoadStateAdapter { bookAdapter.retry() }
            )
        }

        // Observe ViewModel state
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->

                when (state) {
                    is MainUIState.Loading -> showProgressDialog()
                    is MainUIState.Success -> {
                        dismissProgressDialog()
                        bookAdapter.submitData(lifecycle, state.books)

                    }
                    is MainUIState.Error -> {
                        dismissProgressDialog()
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    MainUIState.Idle -> {
                        dismissProgressDialog()
                        // Update based on query state

                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            bookAdapter.loadStateFlow.collect { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> showProgressDialog()
                    is LoadState.NotLoading -> {
                        dismissProgressDialog()
                        updateNoBooksMessage(bookAdapter.itemCount == 0)
                    }

                    is LoadState.Error -> {
                        dismissProgressDialog()
                        val error = (loadStates.refresh as LoadState.Error).error
                        Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        // Setup SearchView
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.processIntent(MainIntent.Search(it)) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                newText?.let { viewModel.processIntent(MainIntent.Search(it)) }
                updateEmptyQueryMessage(newText?:"")
                return true
            }
        })

    }

    private fun updateNoBooksMessage(noBooks: Boolean) {
        binding.noBooksMessage.visibility = if (noBooks) View.VISIBLE else View.GONE
    }

    private fun updateEmptyQueryMessage(query: String) {
        binding.emptyQueryMessage.visibility = if (query.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showProgressDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_progress, null)
        builder.setView(dialogView)

        progressDialog = builder.create()
        progressDialog.show()
    }

    private fun dismissProgressDialog() {
        if (::progressDialog.isInitialized) {
            progressDialog.dismiss()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
