package com.example.costats.ui.countries

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.costats.BaseFragment
import com.example.costats.R
import com.example.costats.databinding.FragmentCountriesBinding
import com.example.costats.models.Country
import com.example.costats.models.Data
import com.example.costats.models.Resource
import com.example.costats.models.Status
import com.example.costats.util.Constants
import com.example.costats.util.extensions.getMainViewModel
import com.example.costats.util.extensions.glide
import com.example.costats.util.extensions.sharedPreferences
import com.example.costats.util.extensions.showShortSnackBar

class CountriesFragment : BaseFragment() {

    companion object {
        private val TAG = CountriesFragment::class.java.simpleName
    }

    //Global
    private lateinit var binding: FragmentCountriesBinding
    private val viewModel by lazy { getMainViewModel() }
    private val glide by lazy { glide() }
    private val sp by lazy { sharedPreferences() }
    private var adapter: CountriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_countries, container, false)
        binding.imgBtnBack.setOnClickListener { onBackPressed() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        viewModel.stats.observe(viewLifecycleOwner, Observer { bindCountries(it) })
    }

    private fun setupRecycler() {
        adapter = CountriesAdapter(glide)
        adapter?.listener = object : CountriesAdapter.Listener {
            override fun onClick(country: Country) {
                sp.edit().putString(Constants.KEY_COUNTRY_CODE, country.code).apply()
                onBackPressed()
            }
        }
        binding.recyclerCountries.layoutManager =
            GridLayoutManager(requireContext(), 3, VERTICAL, false)
        binding.recyclerCountries.adapter = adapter

    }

    private fun bindCountries(resource: Resource<Data>) {
        when (resource.status) {
            Status.LOADING -> Log.d(TAG, "TestLog: ${resource.status}")
            Status.SUCCESS -> {
                val data = resource.data ?: return
                adapter?.swapData(data.countries)
            }
            Status.ERROR -> showShortSnackBar(resource.message)
        }
    }

}