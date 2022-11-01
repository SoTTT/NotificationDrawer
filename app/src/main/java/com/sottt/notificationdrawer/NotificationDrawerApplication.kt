package com.sottt.notificationdrawer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.PerformanceHintManager
import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.data.defined.ApplicationCoreInfo
import com.sottt.notificationdrawer.data.defined.ApplicationPermissionStatus
import com.sottt.notificationdrawer.data.defined.ApplicationSettings
import java.util.*

class NotificationDrawerApplication : Application() {

    private val _launchTime = Date().time
    val launchTime get() = Util.formatTime(_launchTime)

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var mContext: Context

        var applicationLogLevel: Int = Util.LogUtil.VERBOSE

        val applicationSettings =
            ApplicationSettings(
                ApplicationPermissionStatus(
                    notificationPushPermission = false,
                    notificationAccessPermission = false,
                    ignorePowerOptimization = false
                )
            )

        fun applicationContext() = mContext

        private val appCoreInfoCache = HashSet<ApplicationCoreInfo>()

        fun getAppLabelName(packageName: String): String? {
            synchronized(appCoreInfoCache) {
                iniAppCoreInfoCache()
                return appCoreInfoCache.find {
                    it.packageName == packageName
                }?.appName
            }
        }

        @Deprecated("this method is deprecated", ReplaceWith("getAppIcon", "packageName"))
        @SuppressLint("ObsoleteSdkInt", "UseCompatLoadingForDrawables")
        fun getApplicationIcon(packageName: String, context: Context): Bitmap {
            return Util.getApplicationIcon(packageName, context)
        }

        fun getAppIcon(packageName: String): Bitmap? {
            synchronized(appCoreInfoCache) {
                iniAppCoreInfoCache()
                return appCoreInfoCache.find {
                    it.packageName == packageName
                }?.icon
            }
        }

        private fun iniAppCoreInfoCache() {
            if (appCoreInfoCache.isEmpty()) {
                appCoreInfoCache.addAll(Util.getAppCoreInfoList(applicationContext()))
            }
        }

        fun getInstalledAppList(): List<String> =
            Util.getInstalledApp(applicationContext())

    }

    override fun onCreate() {
        super.onCreate()
        //iniAppNameCache()
        mContext = applicationContext
        applicationSettings.permissionStatus.apply {
            notificationAccessPermission = Util.notificationAccessEnable(applicationContext())
            notificationPushPermission = Util.notificationEnable(applicationContext())
            ignorePowerOptimization = Util.ignoreBatteryOptimizations(applicationContext())
        }
        if (applicationSettings != Repository.readSettings()) {
            Repository.writeSettings(applicationSettings)
        }
    }


}