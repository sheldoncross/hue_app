package com.example.android.hue.database.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract val userDatabaseDao: UserDatabaseDao

    companion object{

        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            synchronized(this){
                //Instance of user database
                var instance =
                    INSTANCE

                //If the instance of the user database is empty then build one
                if(instance == null){
                    //Build instance of database using UserDatabase class and name database
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    //INSTANCE equals built instance
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}