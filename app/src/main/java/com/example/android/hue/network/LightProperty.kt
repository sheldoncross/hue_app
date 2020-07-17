package com.example.android.hue.network

data class LightProperty(
    val state : StateList?,
    val name : String
)

data class StateList(
    val on : Boolean,
    val bri : Int,
    val hue : Int,
    val sat : Int
)