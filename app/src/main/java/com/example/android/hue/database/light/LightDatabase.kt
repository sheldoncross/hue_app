package com.example.android.hue.database.light

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Light::class], version = 1, exportSchema = false)
abstract class LightDatabase : RoomDatabase() {

    abstract val lightDatabaseDao: LightDatabaseDao

    companion object{

        @Volatile
        private var INSTANCE: LightDatabase? = null

        fun getInstance(context: Context): LightDatabase {
            synchronized(this){
                //Instance of user database
                var instance =
                    INSTANCE

                //If the instance of the user database is empty then build one
                if(instance == null){
                    //Build instance of database using UserDatabase class and name database
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LightDatabase::class.java,
                        "light_map_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}