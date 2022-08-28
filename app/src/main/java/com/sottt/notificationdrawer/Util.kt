package com.sottt.notificationdrawer

import android.app.Notification
import android.content.Context
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationContext
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationLogLevel
import com.sottt.notificationdrawer.data.defined.NotificationInfo

object Util {

    fun createNullNotification(): NotificationInfo {
        return NotificationInfo(
            "null",
            "null",
            "null",
            0,
            "null"
        )
    }

    fun StatusBarNotification.toNotificationInfo(): NotificationInfo {
        val bundle = this.notification.extras
        val text = bundle.getString(Notification.EXTRA_TEXT)
        val title = bundle.getString(Notification.EXTRA_TITLE)
        return NotificationInfo(
            title ?: "",
            text ?: "",
            postTime.toString(),
            this.id,
            this.packageName
        )
    }

    /**
     * Show a Toast use application context
     *
     * @return unit
     */
    fun showToast(text: CharSequence, time: Int) {
        Toast.makeText(
            applicationContext(),
            text,
            time
        ).show()
    }

    /**
     * Query whether the current application has permission to access notifications
     *
     * @return boolean
     * true :have access notification permission
     * false :have not access notification permission
     */
    fun notificationAccessEnable(): Boolean {
        val packageName: String = applicationContext().packageName
        val flat: String =
            Settings.Secure.getString(
                applicationContext().contentResolver,
                "enabled_notification_listeners"
            )
        return flat.contains(packageName)
    }

    /**
     * Query whether the application has permission to push notification to notification bar
     *
     * @return boolean
     */

    fun notificationEnable(): Boolean {
        return NotificationManagerCompat.from(applicationContext()).areNotificationsEnabled()
    }

    fun ignoreBatteryOptimizations(): Boolean {
        val powerManager =
            applicationContext().getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(applicationContext().packageName)
    }

    fun checkAllPermission(): Boolean =
        ignoreBatteryOptimizations() && notificationEnable() && notificationAccessEnable()

    fun checkNecessaryPermission(): Boolean = notificationAccessEnable() && notificationEnable()

    object LogUtil {

        const val VERBOSE = 1;

        const val DEBUG = 2;

        const val INFO = 3;

        const val WARNING = 4;

        const val ERROR = 5;

        fun v(tag: String, msg: String) {
            if (applicationLogLevel <= VERBOSE) {
                Log.v(tag, msg)
            }
        }

        fun d(tag: String, msg: String) {
            if (applicationLogLevel <= DEBUG) {
                Log.d(tag, msg)
            }
        }

        fun i(tag: String, msg: String) {
            if (applicationLogLevel <= INFO) {
                Log.i(tag, msg)
            }
        }

        fun w(tag: String, msg: String) {
            if (applicationLogLevel <= WARNING) {
                Log.w(tag, msg)
            }
        }

        fun e(tag: String, msg: String) {
            if (applicationLogLevel <= ERROR) {
                Log.e(tag, msg)
            }
        }

    }

    fun isMainThread(): Boolean {
        return Looper.getMainLooper() === Looper.myLooper()
    }

}