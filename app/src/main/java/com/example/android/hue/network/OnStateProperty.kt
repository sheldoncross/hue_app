package com.example.android.hue.network

data class OnStateProperty(
    val state : OnState?
)

data class OnState(
    val on : Boolean
)