package com.example.costats.data.repository.impl

import com.example.costats.data.network.Retrofit
import com.example.costats.data.repository.contract.MainRepository
import com.example.costats.models.Data
import com.example.costats.models.Resource
import com.example.costats.util.extensions.resourceFlow
import kotlinx.coroutines.flow.Flow

class MainRepositoryImpl : MainRepository {

    companion object {
        private val TAG = MainRepositoryImpl::class.java.simpleName
    }

    override fun fetchStats(): Flow<Resource<Data>> {
        return resourceFlow {
            val result = Retrofit.coStatApi.fetchStats()

            if (result.isSuccessful) {
                val feed = result.body()
                emit(Resource.success(feed))
            } else {
                val msg = result.message()
                emit(Resource.error(msg))
            }
        }
    }

}