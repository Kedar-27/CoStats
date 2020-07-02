package com.example.costats.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.costats.data.repository.impl.MainRepositoryImpl
import com.example.costats.models.Data
import com.example.costats.models.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : ViewModel() {

    private val mainRepository by lazy { MainRepositoryImpl() }

    private val _stats = MutableLiveData<Resource<Data>>()
    val stats: LiveData<Resource<Data>> = _stats

    fun fetchStats() {
        mainRepository.fetchStats()
            .onEach { _stats.postValue(it) }
            .launchIn(viewModelScope)
    }

}