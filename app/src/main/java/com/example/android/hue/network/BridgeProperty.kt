package com.example.android.hue.network

data class BridgeProperty(
    val httpCode: Int? = null,
    val error: ErrorList?,
    val success: SuccessString?)

data class ErrorList(
    val type: Int,
    val address: String,
    val description: String
)

data class SuccessString(
    val username: String
)
