package com.example.costats.data.repository.contract

import com.example.costats.models.Data
import com.example.costats.models.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun fetchStats(): Flow<Resource<Data>>

}