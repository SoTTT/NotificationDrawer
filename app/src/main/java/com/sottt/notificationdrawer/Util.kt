package com.sottt.notificationdrawer

import android.content.Context
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationContext
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationLogLevel

object Util {


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
//        val appInformation = applicationContext().applicationInfo
//        val packageName = applicationContext().packageName
//        val uid = appInformation.uid
//        try {
//            val notificationManager =
//                applicationContext().getSystemService(Context.NOTIFICATION_SERVICE)
//            val notificationServiceField =
//                notificationManager.javaClass.getDeclaredMethod("getService");
//            notificationServiceField.isAccessible = true
//            val service = notificationServiceField.invoke(notificationManager)
//
//            val method = service.javaClass.getDeclaredMethod(
//                "areNotificationEnabledForPackage",
//                String::class.java,
//                Int::class.java
//            )
//            method.isAccessible = true
//            return method.invoke(service, packageName, uid) as Boolean
//        } catch (exception: Exception) {
//            exception.printStackTrace()
//            Util.showToast("no",Toast.LENGTH_SHORT)
//        }
//        return false
        return NotificationManagerCompat.from(applicationContext()).areNotificationsEnabled()
    }

    fun ignoreBatteryOptimizations(): Boolean {
        val powerManager =
            applicationContext().getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(applicationContext().packageName)
    }

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
                Log.d(tag, msg)
            }
        }

        fun w(tag: String, msg: String) {
            if (applicationLogLevel <= WARNING) {
                Log.d(tag, msg)
            }
        }

        fun e(tag: String, msg: String) {
            if (applicationLogLevel <= ERROR) {
                Log.d(tag, msg)
            }
        }

    }


}