package com.example.costats.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.costats.data.repository.impl.MainRepositoryImpl
import com.example.costats.models.Data
import com.example.costats.models.Resource

class MainViewModel : ViewModel() {

    private val mainRepository by lazy { MainRepositoryImpl() }

    fun fetchStats(): LiveData<Resource<Data>> = mainRepository.fetchStats().asLiveData()

}