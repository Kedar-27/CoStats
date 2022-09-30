package com.example.costats.data.network

import com.example.costats.BuildConfig.BaseUrl
import com.example.costats.data.network.api.CoStatApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit {

    private val logging: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
//            .addNetworkInterceptor(logging)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val coStatApi: CoStatApi by lazy { retrofit.create(CoStatApi::class.java) }

}