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
import com.fara.bookexplorer.ui.base.BaseFragment
import com.fara.bookexplorer.ui.state.MainUIState
import com.fara.bookexplorer.viewModel.main.MainViewModel
import com.fara.bookexpolorer.R
import com.fara.bookexpolorer.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :BaseFragment<FragmentMainBinding>() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is MainUIState.Loading -> {
                        Log.e("eee", "onViewCreated: on progresss" )
                       binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainUIState.Success -> {

                        val books = state.books
                        Log.e("eee", "onViewCreated: on success ${books?.numFound}" )
                    }
                    is MainUIState.Error -> {
                        // Show error message
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    MainUIState.Idle -> {
                        Log.e("eee", "onViewCreated:  on iDLE", )
                        // Initial state, no action required
                    }
                }
            }
        }




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