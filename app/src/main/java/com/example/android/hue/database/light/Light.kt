package com.example.android.hue.database.light

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "light_map_table")
data class Light(
    @PrimaryKey
    var name: String = "none",

    @ColumnInfo
    var idNumber : Int = -1,

    @ColumnInfo(name = "on")
    var on: Boolean = false,

    @ColumnInfo(name = "bri")
    var bri: Int = -1,

    @ColumnInfo(name = "hue")
    var hue: Int = -1,

    @ColumnInfo(name = "sat")
    var sat: Int = -1
)