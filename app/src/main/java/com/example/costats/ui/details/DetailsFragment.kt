package com.example.costats.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.costats.BaseFragment
import com.example.costats.R
import com.example.costats.databinding.FragmentDetailsBinding

class DetailsFragment : BaseFragment() {

    companion object {
        private val TAG = DetailsFragment::class.java.simpleName
    }

    //Global
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.imgBtnBack.setOnClickListener { onBackPressed() }
        return binding.root
    }

}