package com.example.pollycall.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pollycall.data.Call
import com.example.pollycall.data.CallResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface CallDao {

    @Insert
    fun saveCallCache(call: Call)

    @Query("SELECT * FROM call WHERE number = :number")
    fun getCallCache(number: String): Flow<Call?>

}