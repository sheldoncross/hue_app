package com.example.android.hue.database.light

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LightDatabaseDao {
    @Insert
    suspend fun insert(light: Light)

    @Update
    fun update(light: Light)

    @Query("SELECT * from light_map_table WHERE name = :name")
    fun get(name: String): Light

    @Query("DELETE FROM light_map_table")
    fun clear()

    @Query("SELECT * FROM light_map_table ORDER BY name ASC")
    suspend fun getAllLights(): List<Light>
}