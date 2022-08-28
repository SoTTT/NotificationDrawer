package com.sottt.notificationdrawer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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

    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var dialog: BottomSheetDialog

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
        if (!Util.checkNecessaryPermission()) {
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
        iniBottomSheetDialogCallback()
    }

    private fun iniBottomSheetDialogCallback() {
        dialog = BottomSheetDialog(this)
        val view =
            layoutInflater.inflate(R.layout.notification_information_bottom_sheet_layout, null)
        dialog.setContentView(view)
        behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Util.LogUtil.v(TAG, "new Status: BottomSheetBehavior.STATE_EXPANDED")
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Util.LogUtil.v(TAG, "new Status: BottomSheetBehavior.STATE_HIDDEN")
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Util.LogUtil.v(TAG, "new Status: BottomSheetBehavior.STATE_COLLAPSED")
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        Util.LogUtil.v(TAG, "new Status: BottomSheetBehavior.STATE_DRAGGING")
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        Util.LogUtil.v(TAG, "new Status: BottomSheetBehavior.STATE_HALF_EXPANDED")
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        Util.LogUtil.v(TAG, "new Status: BottomSheetBehavior.STATE_SETTLING")
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    fun showBottomSheetDialog(bundle: Bundle) {
        val title = bundle.getString("TITLE")
        val content = bundle.getString("CONTENT")
        val time = bundle.getString("TIME")
        val packageName = bundle.getString("PACKAGE_NAME")
        dialog.apply {
            findViewById<TextView>(R.id.notification_title)?.text = title
            findViewById<TextView>(R.id.notification_content)?.text = content
            findViewById<TextView>(R.id.notification_time)?.text = time
            findViewById<TextView>(R.id.notification_package_name)?.text = packageName
        }
        dialog.show()
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