package com.example.costats.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Country(
    @SerializedName("Country") val name: String,
    @SerializedName("CountryCode") val code: String,
    @SerializedName("NewConfirmed") val newConfirmed: Long,
    @SerializedName("TotalConfirmed") val totalConfirmed: Long,
    @SerializedName("NewDeaths") val newDeaths: Long,
    @SerializedName("TotalDeaths") val totalDeaths: Long,
    @SerializedName("NewRecovered") val newRecovered: Long,
    @SerializedName("TotalRecovered") val totalRecovered: Long,
    @SerializedName("Date") val date: Date
)