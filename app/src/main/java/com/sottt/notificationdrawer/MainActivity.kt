package com.sottt.notificationdrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sottt.notificationdrawer.databinding.ActivityMainBinding
import com.sottt.notificationdrawer.ui.homeFragment.HomeFragment
import com.sottt.notificationdrawer.ui.welcomeFragment.WelcomeFragment

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(
            viewBinding.center.id, if (Util.checkAllPermission()) {
                HomeFragment()
            } else {
                WelcomeFragment()
            }
        )
        transaction.commit()
    }
}