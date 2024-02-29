package com.pollyanna.pollycall.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pollyanna.pollycall.data.dataclass.Call
import kotlinx.coroutines.flow.Flow

@Dao
interface CallDao {

    @Insert
    fun saveCallCache(call: Call)

    @Query("SELECT * FROM call WHERE number = :number")
    fun getCallCache(number: String): Flow<Call?>

    @Insert
    fun saveScamCallCache(call: Call)

    @Query("SELECT * FROM call WHERE is_scam = 1 ")
    fun getScamCallCache(): Flow<List<Call>>

}