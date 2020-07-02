package com.example.costats.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.costats.BaseFragment
import com.example.costats.R
import com.example.costats.databinding.FragmentCountriesBinding

class CountriesFragment : BaseFragment() {

    companion object {
        private val TAG = CountriesFragment::class.java.simpleName
    }

    //Global
    private lateinit var binding: FragmentCountriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_countries, container, false)
        binding.imgBtnBack.setOnClickListener { onBackPressed() }
        return binding.root
    }

}