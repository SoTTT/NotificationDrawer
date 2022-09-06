package com.sottt.notificationdrawer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.data.defined.ApplicationCoreInfo
import com.sottt.notificationdrawer.data.defined.ApplicationPermissionStatus
import com.sottt.notificationdrawer.data.defined.ApplicationSettings
import kotlinx.coroutines.sync.Mutex
import kotlin.concurrent.thread

class NotificationDrawerApplication : Application() {

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

        private lateinit var appNameCache: HashMap<String, ApplicationCoreInfo>

        fun getAppName(packageName: String): String {
            return appNameCache[packageName]?.appName ?: "NULL"
        }

        private val lockForAppNameCache = Mutex()
    }

    private fun iniAppNameCache() {
        thread {
            synchronized(lockForAppNameCache) {
                appNameCache = Util.packageNameToAppName(applicationContext)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        iniAppNameCache()
        mContext = applicationContext
        applicationSettings.permissionStatus.apply {
            notificationAccessPermission = Util.notificationAccessEnable()
            notificationPushPermission = Util.notificationEnable()
            ignorePowerOptimization = Util.ignoreBatteryOptimizations()
        }
        Repository.create()
    }

}