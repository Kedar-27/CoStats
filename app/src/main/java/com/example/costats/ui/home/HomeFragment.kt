package com.example.costats.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.costats.BaseFragment
import com.example.costats.R
import com.example.costats.databinding.FragmentHomeBinding
import com.example.costats.models.Country
import com.example.costats.models.Data
import com.example.costats.models.Resource
import com.example.costats.models.Status
import com.example.costats.util.extensions.getMainViewModel
import com.example.costats.util.extensions.showShortSnackBar

class HomeFragment : BaseFragment() {

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }

    //Global
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by lazy { getMainViewModel() }
    private var adapter: StatsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

        setupRecycler()

        viewModel.stats.observe(viewLifecycleOwner, Observer { bindData(it) })

    }

    private fun setupListeners() {

        binding.imgCountry.setOnClickListener { openCountries() }

        binding.imgBtnFilter.setOnClickListener { openFilterSheet() }

        binding.imgBtnSort.setOnClickListener { openSortSheet() }
    }

    private fun setupRecycler() {
        adapter = StatsAdapter()
        adapter?.listener = object : StatsAdapter.Listener {
            override fun onClick(country: Country) {
                openDetails(country.code)
            }
        }
        binding.recyclerStats.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStats.adapter = adapter
    }

    private fun openCountries() {
        val action = HomeFragmentDirections.navigateHomeToCountries()
        findNavController().navigate(action)
    }

    private fun openFilterSheet() {
        Log.d(TAG, "TestLog: openFilterSheet")
    }

    private fun openSortSheet() {
        Log.d(TAG, "TestLog: openSortSheet")
    }

    private fun openDetails(code: String) {
        val action = HomeFragmentDirections.navigateHomeToDetails(code)
        findNavController().navigate(action)
    }

    private fun bindData(resource: Resource<Data>) {
        when (resource.status) {
            Status.LOADING -> Log.d(TAG, "TestLog: ${resource.status}")
            Status.SUCCESS -> {
                val data = resource.data ?: return

                val date = data.date
                Log.d(TAG, "TestLog: d:$date")

                val global = data.global
                Log.d(TAG, "TestLog: g:$global")

                adapter?.swapData(data.countries)

            }
            Status.ERROR -> showShortSnackBar(resource.message)
        }
    }

}