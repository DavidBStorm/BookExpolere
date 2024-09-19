package com.fara.bookexplorer.ui.fragments.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.fara.bookexplorer.ui.adapter.BookAdapter
import com.fara.bookexplorer.ui.adapter.BookLoadStateAdapter
import com.fara.bookexplorer.ui.base.BaseFragment
import com.fara.bookexplorer.ui.fragments.detail.DetailsFragment
import com.fara.bookexplorer.ui.state.MainIntent
import com.fara.bookexplorer.ui.state.MainUIState
import com.fara.bookexplorer.viewModel.main.MainViewModel
import com.fara.bookexpolorer.R
import com.fara.bookexpolorer.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

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


        initView()
        observeItems()


    }

    private fun observeItems() {
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
                        Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }

    private fun initView() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        bookAdapter = BookAdapter { book ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DetailsFragment.newInstance(book))
                .addToBackStack(DetailsFragment::class.java.name)
                .commit()
        }
        binding.searchView.apply {

            setOnClickListener {
                this.isIconified = false
                this.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(this.findFocus(), InputMethodManager.SHOW_IMPLICIT)
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookAdapter.withLoadStateFooter(
                footer = BookLoadStateAdapter { bookAdapter.retry() }
            )
        }

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.processIntent(MainIntent.Search(it)) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                newText?.let { viewModel.onQueryTextChanged(it) }
                updateEmptyQueryMessage(newText ?: "")
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
