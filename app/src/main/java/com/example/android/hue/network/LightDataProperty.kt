package com.example.android.hue.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LightDataProperty(var on: Boolean = true)