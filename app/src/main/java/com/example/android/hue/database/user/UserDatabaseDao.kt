package com.example.android.hue.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDatabaseDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * from users WHERE username = :username")
    fun get(username: String): User

    @Query("DELETE FROM users")
    fun clear()

    @Query("SELECT * FROM users ORDER BY username DESC LIMIT 1")
    suspend fun getDefaultUser(): User
}