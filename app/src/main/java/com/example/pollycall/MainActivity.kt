package com.example.pollycall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.databinding.DataBindingUtil
import com.example.pollycall.databinding.ActivityMainBinding
import com.example.pollycall.ui.theme.PollyCallTheme

class MainActivity : ComponentActivity() {
    lateinit var bindiing: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindiing = DataBindingUtil.setContentView(this, R.layout.activity_main)


    }
}

