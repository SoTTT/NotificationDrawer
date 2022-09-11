package com.sottt.notificationdrawer.setting.ui

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.sottt.notificationdrawer.NotificationDrawerApplication
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.Util

class AppSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val preference = findPreference<SwitchPreference>("permission_access_notification")
        preference?.setOnPreferenceClickListener {
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
            true
        }

        val preference2 = findPreference<SwitchPreference>("permission_push_notification")
        preference2?.setOnPreferenceClickListener {
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
            true
        }

        val preference3 =
            findPreference<SwitchPreference>("permission_ignore_battery_optimizations")
        preference3?.setOnPreferenceClickListener {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data =
                    Uri.parse("package:${NotificationDrawerApplication.applicationContext().packageName}")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                exception.printStackTrace()
                try {
                    val intent = Intent()
                    intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                } catch (exception: ActivityNotFoundException) {
                    exception.printStackTrace()
                    Util.showToast("跳转失败", Toast.LENGTH_LONG)
                }
            }
            true
        }
    }

    companion object {
        fun getPreference(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
    }

}