package com.sottt.notificationdrawer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.data.defined.ApplicationPermissionStatus
import com.sottt.notificationdrawer.data.defined.ApplicationSettings

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

    }


    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        applicationSettings.permissionStatus.apply {
            notificationAccessPermission = Util.notificationAccessEnable()
            notificationPushPermission = Util.notificationEnable()
            ignorePowerOptimization = Util.ignoreBatteryOptimizations()
        }
        Repository.create()
    }

}