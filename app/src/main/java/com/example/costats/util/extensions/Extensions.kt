package com.example.costats.util.extensions

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.costats.ui.MainViewModel
import com.example.costats.util.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
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

fun Fragment.getBottomSheet(contentView: View): BottomSheetDialog =
    requireActivity().getBottomSheet(contentView)

fun FragmentActivity.getBottomSheet(contentView: View): BottomSheetDialog =
    BottomSheetDialog(this).apply { setContentView(contentView) }

fun Fragment.glide() = Glide.with(this)

fun Fragment.sharedPreferences(): SharedPreferences = requireActivity().sharedPreferences()

fun FragmentActivity.sharedPreferences(): SharedPreferences =
    application.getSharedPreferences(Constants.COSTATS_SHARED_PREFS, Context.MODE_PRIVATE)