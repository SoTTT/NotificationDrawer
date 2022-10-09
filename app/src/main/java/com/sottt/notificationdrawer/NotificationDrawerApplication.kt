package com.sottt.notificationdrawer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.data.defined.ApplicationCoreInfo
import com.sottt.notificationdrawer.data.defined.ApplicationPermissionStatus
import com.sottt.notificationdrawer.data.defined.ApplicationSettings
import kotlinx.coroutines.sync.Mutex
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

        private val appNameCache = HashSet<ApplicationCoreInfo>()

        fun getAppName(packageName: String): String? {
            synchronized(appNameCache) {
                if (appNameCache.isEmpty()) {
                    appNameCache.addAll(Util.getAppCoreInfoList(applicationContext()))
                }
                return appNameCache.find {
                    it.packageName == packageName
                }?.appName
            }
        }

        @SuppressLint("ObsoleteSdkInt", "UseCompatLoadingForDrawables")
        fun getApplicationIcon(packageName: String, context: Context): Bitmap {
            return Util.getApplicationIcon(packageName, context)
        }

        fun getAppIcon(packageName: String): Bitmap? {
            synchronized(appNameCache) {
                if (appNameCache.isEmpty()) {
                    appNameCache.addAll(Util.getAppCoreInfoList(applicationContext()))
                }
                return appNameCache.find {
                    it.packageName == packageName
                }?.icon
            }
        }

        fun getInstalledAppList(): List<String> =
            Util.getInstalledApp(applicationContext())

    }


//    private fun iniAppNameCache() {
//        thread {
//            synchronized(lockForAppNameCache) {
//                appNameCache = Util.packageNameToAppName(applicationContext())
//            }
//        }
//    }

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