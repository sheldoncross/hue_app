package com.example.android.hue.network

import com.squareup.moshi.JsonClass

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

@JsonClass (generateAdapter = true)
data class LightData(var on: Boolean = true)