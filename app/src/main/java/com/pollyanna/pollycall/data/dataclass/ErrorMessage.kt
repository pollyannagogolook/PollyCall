package com.pollyanna.pollycall.data.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorMessage(
    val title: String = "",
    val message: String = ""
): Parcelable
