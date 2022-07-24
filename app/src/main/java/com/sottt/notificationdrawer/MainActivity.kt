package com.sottt.notificationdrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sottt.notificationdrawer.databinding.ActivityMainBinding
import com.sottt.notificationdrawer.ui.homeFragment.HomeFragment
import com.sottt.notificationdrawer.ui.welcomeFragment.WelcomeFragment
import kotlin.reflect.KClass

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
            viewBinding.center.id, if (Util.checkNecessaryPermission()) {
                HomeFragment()
            } else {
                WelcomeFragment()
            }
        )
        transaction.commit()
    }


    fun replaceFragmentTo(id: Int, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(id, fragment)
        transaction.commit()
    }

    fun replaceFragmentToCenterLayout(fragment: Fragment) {
        replaceFragmentTo(viewBinding.center.id, fragment)
    }
}