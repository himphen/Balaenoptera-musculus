package com.himphen.myapplication.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiServiceModel(
    val id: String,
    val name: String,
    val symbol: String,
    val code: String? = null,
)