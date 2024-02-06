package com.pollyanna.pollycall.data.local

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import com.pollyanna.pollycall.data.Call

@Database(entities = [Call::class], version = 1)
abstract class CallDatabase : RoomDatabase() {
    abstract fun callDao(): CallDao


    companion object {
        @Volatile
        private var instance: CallDatabase? = null

        fun getInstance(context: Context): CallDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }


        private fun buildDatabase(context: Context): CallDatabase {
            return Room.databaseBuilder(
                context = context,
                CallDatabase::class.java,
                "call-database"
            ).build()
        }
    }
}