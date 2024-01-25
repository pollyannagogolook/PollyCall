package com.example.pollycall.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pollycall.data.Call

@Dao
interface CallDao {

    @Insert
    fun saveCallCache(call: Call)

    @Query("SELECT * FROM call WHERE number = :number")
    fun getCallCache(number: String): Call?

}