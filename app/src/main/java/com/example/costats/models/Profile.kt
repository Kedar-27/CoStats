package com.example.costats.models
import com.google.gson.annotations.SerializedName
import java.util.*

data class UserProfile(
    @SerializedName("name") val name: String,
    @SerializedName("CountryCode") val code: String,
    @SerializedName("isValid") val isValid: Long,
    @SerializedName("image") val ImageURL: Long,
    @SerializedName("age") val age: Long,
    @SerializedName("Date") val date: Date
)