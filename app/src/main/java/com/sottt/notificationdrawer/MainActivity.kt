package com.sottt.notificationdrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sottt.notificationdrawer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}