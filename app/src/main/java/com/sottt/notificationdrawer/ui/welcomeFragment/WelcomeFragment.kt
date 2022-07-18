package com.sottt.notificationdrawer.ui.welcomeFragment

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sottt.notificationdrawer.NotificationDrawerApplication
import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {

    private var _viewBinding: FragmentWelcomeBinding? = null

    private val viewBinding get() = _viewBinding!!

    private lateinit var viewModel: WelcomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.flushPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniView()
        iniViewModel()
    }

    private fun iniViewModel() {

        viewModel = ViewModelProvider(this).get(WelcomeFragmentViewModel::class.java)

        viewModel.isNotificationAccessPermissionEnable.observe(viewLifecycleOwner) {
            viewBinding.notificationAccessPermissionCheckBox.isChecked = it
        }

        viewModel.isAllowedToNotify.observe(viewLifecycleOwner) {
            viewBinding.notificationPushPermissionCheckBox.isChecked = it
        }

        viewModel.isIgnoreBatteryOptimization.observe(viewLifecycleOwner) {
            viewBinding.backstageRunningCheckBox.isChecked = it
        }
    }

    @SuppressLint("BatteryLife")
    private fun iniView() {

        viewBinding.notificationAccessPermission.setOnClickListener {
            try {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                try {
                    val intent = Intent()
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val cn = ComponentName(
                        "com.android.settings",
                        "com.android.settings.Settings.NotificationAccessSettingsActivity"
                    )
                    intent.component = cn
                    intent.putExtra(":settings:show_fragment", "NotificationAccessSettings")
                    context?.startActivity(intent)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    Util.showToast("跳转失败", Toast.LENGTH_LONG)
                }
            }
        }

        viewBinding.notificationAccessPermissionCheckBox.setOnClickListener {
            try {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                try {
                    val intent = Intent()
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val cn = ComponentName(
                        "com.android.settings",
                        "com.android.settings.Settings.NotificationAccessSettingsActivity"
                    )
                    intent.component = cn
                    intent.putExtra(":settings:show_fragment", "NotificationAccessSettings")
                    context?.startActivity(intent)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    Util.showToast("跳转失败", Toast.LENGTH_LONG)
                    viewModel.flushAccessNotificationPermission()
                }
            }
        }

        viewBinding.backstageRunningCheckBox.setOnClickListener {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                exception.printStackTrace()
                Util.showToast("跳转失败", Toast.LENGTH_LONG)
                viewModel.flushIgnoreBatteryOptimization()
            }
        }

        viewBinding.backstageRunning.setOnClickListener {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data =
                    Uri.parse("package:${NotificationDrawerApplication.applicationContext().packageName}")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                exception.printStackTrace()
                Util.showToast("跳转失败", Toast.LENGTH_LONG)
            }
        }

        val onClick = View.OnClickListener {
            try {
                val packageName = NotificationDrawerApplication.applicationContext().packageName
                val appInformation =
                    NotificationDrawerApplication.applicationContext().applicationInfo
                val intent = Intent()
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                //Android 5
                intent.putExtra("app_package", packageName)
                intent.putExtra("app_uid", appInformation.uid)
                //Android 8 +
                intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    val intent = Intent()
                    val packageName = NotificationDrawerApplication.applicationContext().packageName
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (exception: ActivityNotFoundException) {
                    exception.printStackTrace()
                    Util.showToast("跳转失败", Toast.LENGTH_SHORT)
                    viewModel.flushNotificationPermission()
                }
            }

        }

        viewBinding.notificationPushPermissionCheckBox.setOnClickListener(onClick)

        viewBinding.notificationPushPermission.setOnClickListener(onClick)

    }


}