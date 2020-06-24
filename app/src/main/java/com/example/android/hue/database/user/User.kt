package com.example.android.hue.database.user

/*
* The Entity class to represent a user object within the Room database
*/

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    //Unique username string obtained from hue bridge on initial initialization
    @PrimaryKey(autoGenerate = false)
    var username: String = "none"
)