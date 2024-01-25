package com.example.pollycall.local

import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.pollycall.data.Call

@Database(entities = [Call::class], version = 1)
abstract class CallDatabase : RoomDatabase(){
    abstract fun callDao(): CallDao
}