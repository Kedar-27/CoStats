package com.example.costats.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.costats.BaseFragment
import com.example.costats.R
import com.example.costats.databinding.FragmentDetailsBinding
import com.example.costats.models.Country
import com.example.costats.util.Constants
import com.example.costats.util.extensions.getMainViewModel
import com.example.costats.util.extensions.showShortSnackBar

class DetailsFragment : BaseFragment() {

    companion object {
        private val TAG = DetailsFragment::class.java.simpleName
    }

    //Global
    private lateinit var binding: FragmentDetailsBinding
    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel by lazy { getMainViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.imgBtnBack.setOnClickListener { onBackPressed() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val code = args.code

        val country = viewModel.getDetails(code)
        if (country != null) bindCountry(country)
        else {
            showShortSnackBar(Constants.REQUEST_FAILED_MESSAGE)
            onBackPressed()
        }

    }

    private fun bindCountry(country: Country) {
        binding.txtCountry.text = country.name
        binding.txtTotalDeaths.text = "${country.totalDeaths}"
        binding.txtNewDeaths.text = "${country.newDeaths}"
        binding.txtTotalCases.text = "${country.totalConfirmed}"
        binding.txtNewCases.text = "${country.newConfirmed}"
        binding.txtTotalRecovered.text = "${country.totalRecovered}"
        binding.txtNewRecovered.text = "${country.newRecovered}"
    }

}