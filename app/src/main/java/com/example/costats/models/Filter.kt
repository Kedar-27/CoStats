package com.example.costats.models

data class Filter(
    val type: Type? = null,
    val number: Int = -1,
    val condition: Condition? = null
)

enum class Type {
    CASES,
    DEATHS,
    RECOVERED
}

enum class Condition {
    LESS_THAN,
    MORE_THAN
}

enum class FilterState {
    APPLIED,
    CLEARED
}