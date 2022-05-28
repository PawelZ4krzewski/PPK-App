package com.example.ubi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class PPKDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: PPKDatabase? = null

        fun getDatabase(context: Context): PPKDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PPKDatabase::class.java,
                    "ppk_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}