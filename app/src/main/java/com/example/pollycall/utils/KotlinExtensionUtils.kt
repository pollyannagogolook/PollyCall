package com.example.pollycall.utils

import com.airbnb.lottie.BuildConfig


fun Throwable.throwIfDebugBuild() {
    if (BuildConfig.DEBUG) {
        throw this
    }
    throw Exception("${this.message}")
}