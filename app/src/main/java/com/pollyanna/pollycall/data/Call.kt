package com.pollyanna.pollycall.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Entity(tableName = "call")
@Parcelize
data class Call(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "number")
    val number: String,

    @ColumnInfo(name = "owner")
    val owner: String,

    @ColumnInfo(name = "is_scam")
    @Json(name = "is_scam")
    val isScam: Boolean
): Parcelable