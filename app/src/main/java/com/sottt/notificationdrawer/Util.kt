package com.sottt.notificationdrawer

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationContext
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationLogLevel
import com.sottt.notificationdrawer.data.defined.ApplicationCoreInfo
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import java.util.*


object Util {

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

    fun createNullNotification(): NotificationInfo {
        return NotificationInfo(
            "null",
            "null",
            "null",
            0,
            "null",
            "null"
        )
    }

    @SuppressLint("ObsoleteSdkInt", "UseCompatLoadingForDrawables")
    fun getApplicationIcon(packageName: String, context: Context): Bitmap {
        val packageManager = context.packageManager
        var otherContext =
            context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
        val applicationInfo =
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val resources = packageManager.getResourcesForApplication(applicationInfo)
        var id = applicationInfo.icon
        //如果获取到的icon资源值为0，就用本应用的默认图标代替
        if (id == 0) {
            id = R.mipmap.ic_launcher
            otherContext = context
        }
        val bitmap = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val vectorDrawable: Drawable? =
                otherContext.resources.getDrawable(id, otherContext.theme)
            if (otherContext == null) {
                throw Resources.NotFoundException()
            } else {
                vectorDrawable as Drawable
                val bitmap = Bitmap.createBitmap(
                    vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
                vectorDrawable.draw(canvas)
                bitmap
            }
        } else {
            BitmapFactory.decodeResource(resources, id)
        }
        return bitmap
    }

    fun StatusBarNotification.toNotificationInfo(): NotificationInfo {
        val bundle = this.notification.extras
        val text = bundle.getString(Notification.EXTRA_TEXT)
        val title = bundle.getString(Notification.EXTRA_TITLE)
        val timeLong = this.postTime
        val timeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(timeLong)
        val formatTime = timeFormatter.format(date)
        return NotificationInfo(
            title ?: "",
            text ?: "",
            formatTime,
            this.id,
            this.packageName,
            this.key,
            //getApplicationIcon(this.packageName, applicationContext())
            NotificationDrawerApplication.getAppIcon(this.packageName)!!
        )
    }

    fun formatTime(time: Long): String {
        val timeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(time)
        return timeFormatter.format(date)
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
    fun notificationAccessEnable(context: Context): Boolean {
        val packageName: String = context.packageName
        val flat: String =
            Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )
        return flat.contains(packageName)
    }

    /**
     * Query whether the application has permission to push notification to notification bar
     *
     * @return boolean
     */

    fun notificationEnable(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun ignoreBatteryOptimizations(context: Context): Boolean {
        val powerManager =
            context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun checkAllPermission(context: Context): Boolean {
        return ignoreBatteryOptimizations(context) && notificationEnable(context) && notificationAccessEnable(
            context
        )
    }

    fun checkNecessaryPermission(context: Context): Boolean {
        return notificationAccessEnable(context) && notificationEnable(context)
    }

    fun isMainThread(): Boolean {
        return Looper.getMainLooper() === Looper.myLooper()
    }

    fun getAppCoreInfoList(
        context: Context,
    ): List<ApplicationCoreInfo> {
        val packageInfoList = context.packageManager.getInstalledPackages(
            PackageManager.GET_ACTIVITIES or
                    PackageManager.GET_SERVICES
        )
        return packageInfoList.map {
            ApplicationCoreInfo(
                it.packageName,
                context.packageManager.getApplicationLabel(it.applicationInfo).toString(),
                getApplicationIcon(it.packageName, context)
            )
        }
    }


    fun getInstalledApp(context: Context): List<String> {
        val packages: MutableList<String> = ArrayList()
        try {
            val packageInfo: List<PackageInfo> = context.packageManager.getInstalledPackages(
                PackageManager.GET_ACTIVITIES or
                        PackageManager.GET_SERVICES
            )
            for (info in packageInfo) {
                val pkg = info.packageName
                packages.add(pkg)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return packages
    }

}