package com.example.costats.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.costats.R
import com.example.costats.databinding.ActivityMainBinding
import com.example.costats.models.Data
import com.example.costats.models.Resource
import com.example.costats.models.Status
import com.example.costats.util.extensions.getMainViewModel
import com.example.costats.util.extensions.showShortSnackBar

class MainActivity : FragmentActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    //Global
    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy { getMainViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val colorBackground = resources.getColor(R.color.color_background)
            window.statusBarColor = colorBackground
            window.navigationBarColor = colorBackground
            val isNightMode =
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> true
                    else -> false
                }
            if (!isNightMode) {
                window.decorView.systemUiVisibility =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.fetchStats().observe(this, Observer { bindData(it) })

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

                val countries = data.countries
                countries.forEach { c -> Log.d(TAG, "TestLog: c:$c") }

            }
            Status.ERROR -> showShortSnackBar(resource.message)
        }
    }
}