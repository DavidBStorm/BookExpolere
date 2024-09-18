package com.fara.bookexplorer.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fara.bookexplorer.ui.base.BaseFragment
import com.fara.bookexpolorer.R
import com.fara.bookexpolorer.databinding.FragmentMainBinding

class MainFragment :BaseFragment<FragmentMainBinding>() {


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}