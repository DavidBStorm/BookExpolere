package com.fara.bookexplorer.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    // ViewDataBinding variable
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using the abstract method implemented in child fragments
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    // Abstract method to initialize binding in the child fragment
    protected abstract fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): VB

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks
        _binding = null
    }
}
