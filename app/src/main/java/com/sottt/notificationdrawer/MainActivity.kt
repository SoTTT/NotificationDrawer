package com.sottt.notificationdrawer

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.service.notification.StatusBarNotification
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import com.sottt.notificationdrawer.databinding.ActivityMainBinding
import com.sottt.notificationdrawer.service.NotificationListener
import com.sottt.notificationdrawer.ui.homeFragment.HomeFragment
import com.sottt.notificationdrawer.ui.homeFragment.HomeFragmentViewModel
import com.sottt.notificationdrawer.ui.welcomeFragment.WelcomeFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "NOTIFICATION_MAIN_ACTIVITY"
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
    }

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var notificationListenerBinder: NotificationListener.NotificationListenBinder

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Util.LogUtil.d(TAG, "MainActivity: service bind")
            notificationListenerBinder = service as NotificationListener.NotificationListenBinder
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindListenerService()
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
        Util.LogUtil.d(TAG, "MainActivity Created")
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
        startForegroundService(intent)
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