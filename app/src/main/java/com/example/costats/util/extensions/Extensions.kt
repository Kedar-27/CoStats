package com.example.costats.util.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.costats.ui.MainViewModel
import com.example.costats.util.Constants
import com.google.android.material.snackbar.Snackbar

fun Fragment.getMainViewModel(): MainViewModel = requireActivity().getMainViewModel()

fun FragmentActivity.getMainViewModel(): MainViewModel = ViewModelProvider(this)
    .get(MainViewModel::class.java)

fun Fragment.showShortSnackBar(message: String?) = requireActivity().showShortSnackBar(message)

fun FragmentActivity.showShortSnackBar(message: String?) {
    Snackbar.make(
        findViewById(android.R.id.content),
        message ?: Constants.REQUEST_FAILED_MESSAGE,
        Snackbar.LENGTH_SHORT
    ).show()
}