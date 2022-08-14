package com.sottt.notificationdrawer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sottt.notificationdrawer.databinding.ActivityMainBinding
import com.sottt.notificationdrawer.service.NotificationListener
import com.sottt.notificationdrawer.ui.homeFragment.HomeFragment
import com.sottt.notificationdrawer.ui.welcomeFragment.WelcomeFragment
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var notificationListenerBinder: NotificationListener.NotificationListenBinder

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
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

    fun startListenerService() {
        val intent = Intent(this, NotificationListener::class.java)
        startService(intent)
    }

    fun stopListenerService() {
        val intent = Intent(this, NotificationListener::class.java)
        stopService(intent)
    }

    fun bindListenerService() {
        val intent = Intent(this, NotificationListener::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun unbindListenerService() {
        unbindService(connection)
    }
}