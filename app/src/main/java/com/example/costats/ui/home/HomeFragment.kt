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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.costats.BaseFragment
import com.example.costats.R
import com.example.costats.databinding.FragmentHomeBinding
import com.example.costats.models.*
import com.example.costats.util.Constants
import com.example.costats.util.extensions.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_filter.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_sort.view.*

class HomeFragment : BaseFragment() {

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }

    //Global
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by lazy { getMainViewModel() }
    private val glide by lazy { glide() }
    private val sp by lazy { sharedPreferences() }
    private var code: String? = null
    private var adapter: StatsAdapter? = null
    private var masterList = listOf<Country>()
    private var currentList = mutableListOf<Country>()

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

        code = sp.getString(Constants.KEY_COUNTRY_CODE, null)

        if (code != null) {
            val url = "https://www.countryflags.io/${code}/flat/64.png"
            glide.load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgCountry)
        }

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
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_bottom_sheet_filter, null)
        selectFilterType(view, viewModel.filter?.type)
        selectFilterCondition(view, viewModel.filter?.condition)
        val value = viewModel.filter?.number
        if (value != null && value > -1) view.edit_value.setText("$value")
        val dialog = getBottomSheet(view)
        var state: FilterState? = null
        var filter = viewModel.filter ?: Filter()
        view.txt_btn_clear.setOnClickListener {
            state = FilterState.CLEARED
            dialog.dismiss()
        }
        view.txt_btn_done.setOnClickListener {
            state = FilterState.APPLIED
            dialog.dismiss()
        }
        view.txt_btn_cases.setOnClickListener {
            filter = filter.copy(type = Type.CASES)
            selectFilterType(view, filter.type)
        }
        view.txt_btn_deaths.setOnClickListener {
            filter = filter.copy(type = Type.DEATHS)
            selectFilterType(view, filter.type)
        }
        view.txt_btn_recovered.setOnClickListener {
            filter = filter.copy(type = Type.RECOVERED)
            selectFilterType(view, filter.type)
        }
        view.text_btn_less_than.setOnClickListener {
            filter = filter.copy(condition = Condition.LESS_THAN)
            selectFilterCondition(view, filter.condition)
        }
        view.text_btn_greater_than.setOnClickListener {
            filter = filter.copy(condition = Condition.MORE_THAN)
            selectFilterCondition(view, filter.condition)
        }
        dialog.setOnDismissListener {
            when (state) {
                FilterState.CLEARED -> {
                    viewModel.filter = null
                    applyFilter()
                }
                FilterState.APPLIED -> {
                    if (isValid(view)) {
                        viewModel.filter =
                            filter.copy(number = view.edit_value!!.text.toString().toInt())
                        applyFilter()
                    } else showShortSnackBar("Please enter a value!")
                }
            }
        }
        dialog.show()
    }

    private fun openSortSheet() {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_bottom_sheet_sort, null)
        selectSort(view)
        val dialog = getBottomSheet(view)
        view.txt_btn_asc.setOnClickListener {
            viewModel.sort = Sort.ASC
            applySort()
            dialog.dismiss()
        }
        view.txt_btn_desc.setOnClickListener {
            viewModel.sort = Sort.DESC
            applySort()
            dialog.dismiss()
        }
        dialog.show()
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
                masterList = data.countries.toMutableList()
                applyFilter()
                applySort()
            }
            Status.ERROR -> showShortSnackBar(resource.message)
        }
    }

    /**
     * Filters
     * */
    private fun selectFilterType(view: View, type: Type?) {
        when (type) {
            Type.CASES -> {
                view.txt_btn_cases.isSelected = true
                view.txt_btn_deaths.isSelected = false
                view.txt_btn_recovered.isSelected = false
            }
            Type.DEATHS -> {
                view.txt_btn_cases.isSelected = false
                view.txt_btn_deaths.isSelected = true
                view.txt_btn_recovered.isSelected = false
            }
            Type.RECOVERED -> {
                view.txt_btn_cases.isSelected = false
                view.txt_btn_deaths.isSelected = false
                view.txt_btn_recovered.isSelected = true
            }
        }
    }

    private fun selectFilterCondition(view: View, condition: Condition?) {
        when (condition) {
            Condition.LESS_THAN -> {
                view.text_btn_less_than.isSelected = true
                view.text_btn_greater_than.isSelected = false
            }
            Condition.MORE_THAN -> {
                view.text_btn_less_than.isSelected = false
                view.text_btn_greater_than.isSelected = true
            }
        }
    }

    private fun isValid(view: View): Boolean {
        var isValid = true

        if (view.edit_value?.text.isNullOrEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun applyFilter() {
        when (viewModel.filter?.condition) {
            Condition.LESS_THAN -> filterLessThan()
            Condition.MORE_THAN -> filterMoreThan()
            else -> currentList = masterList.toMutableList()
        }
        applySort()
    }

    private fun filterLessThan() {
        currentList = masterList.toMutableList()
        val filter = viewModel.filter
        when (filter?.type) {
            Type.CASES -> currentList.filter { c -> c.totalConfirmed <= filter.number }
            Type.DEATHS -> currentList.filter { c -> c.totalDeaths <= filter.number }
            Type.RECOVERED -> currentList.filter { c -> c.totalRecovered <= filter.number }
        }
    }

    private fun filterMoreThan() {
        currentList = masterList.toMutableList()
        val filter = viewModel.filter
        when (filter?.type) {
            Type.CASES -> currentList.filter { c -> c.totalConfirmed >= filter.number }
            Type.DEATHS -> currentList.filter { c -> c.totalDeaths >= filter.number }
            Type.RECOVERED -> currentList.filter { c -> c.totalRecovered >= filter.number }
        }
    }

    /**
     * Sort
     * */
    private fun selectSort(view: View) {
        when (viewModel.sort) {
            Sort.ASC -> {
                view.txt_btn_desc.isSelected = false
                view.txt_btn_asc.isSelected = true
            }
            Sort.DESC -> {
                view.txt_btn_asc.isSelected = false
                view.txt_btn_desc.isSelected = true
            }
        }
    }

    private fun applySort() {
        when (viewModel.sort) {
            Sort.ASC -> currentList.sortBy { c -> c.totalConfirmed }
            Sort.DESC -> currentList.sortByDescending { c -> c.totalConfirmed }
        }
        if (code != null) {
            val country = masterList.find { c -> c.code == code }
            if (country != null) {
                currentList.remove(country)
                currentList.add(0, country)
            }
        }
        adapter?.swapData(currentList)
    }

}