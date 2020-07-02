package com.example.costats.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Data(
    @SerializedName("Global") val global: Global,
    @SerializedName("Countries") val countries: List<Country>,
    @SerializedName("Date") val date: Date
)