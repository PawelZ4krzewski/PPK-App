package com.example.ubi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ubi.database.payment.PaymentDao
import com.example.ubi.database.payment.Payment
import com.example.ubi.database.user.User
import com.example.ubi.database.user.UserDao

@Database(entities = [User::class, Payment::class], version = 1, exportSchema = false)
abstract class PPKDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun PaymentDao(): PaymentDao

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