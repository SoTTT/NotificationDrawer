package com.sottt.notificationdrawer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.sottt.notificationdrawer.databinding.ActivityMainBinding
import com.sottt.notificationdrawer.service.NotificationListener
import com.sottt.notificationdrawer.ui.homeFragment.HomeFragmentViewModel

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
        Util.LogUtil.d(TAG, "MainActivity Created")
//        val fragmentManager = supportFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//        transaction.replace(
//            viewBinding.center.id, HomeFragment()
//        )
//        transaction.commit()

    }

    override fun onRestart() {
        super.onRestart()
        if (Util.checkNecessaryPermission()) {

        } else {
            unbindListenerService()
            stopListenerService()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val navView = viewBinding.nav
        val navController = findNavController(R.id.main_activity_center)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.statisticalFragment
            )
        )
        navView.setupWithNavController(navController)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }


    fun replaceFragmentTo(id: Int, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(id, fragment)
        transaction.commit()
    }

    fun replaceFragmentToCenterLayout(fragment: Fragment) {
        replaceFragmentTo(viewBinding.mainActivityCenter.id, fragment)
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