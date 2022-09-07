package com.sottt.notificationdrawer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
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
                    vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
                    vectorDrawable.draw(canvas)
                    bitmap
                }

            } else {
                BitmapFactory.decodeResource(resources, id)
            }
            return bitmap
        }
    }

    fun getAppIcon(packageName: String): Bitmap {
        return getApplicationIcon(packageName, applicationContext())
    }

    private fun iniAppNameCache() {
        thread {
            synchronized(lockForAppNameCache) {
                appNameCache = Util.packageNameToAppName(applicationContext())
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