package com.sottt.notificationdrawer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.MainActivity
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.Util.LogUtil
import com.sottt.notificationdrawer.Util.createNullNotification
import com.sottt.notificationdrawer.Util.toNotificationInfo
import com.sottt.notificationdrawer.data.defined.FilterInfo
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import com.sottt.notificationdrawer.filter.AbstractFilter
import com.sottt.notificationdrawer.filter.FilterCollection
import com.sottt.notificationdrawer.filter.NotificationFilterHandler


class NotificationListener : NotificationListenerService() {

    companion object {
        const val TAG = "NotificationListener"
    }

    private val mBinder = NotificationListenBinder()

    val isFilterValid: Boolean get() = filterHandler.isValid

    inner class NotificationListenBinder : Binder() {

        fun getNotification() = activeNotifications.toList()

        fun cancelNotification(key: String) {
            cancelOneNotification(key)
        }

        fun setFilterValid(valid: Boolean) {
            if (valid != isFilterValid)
                filterHandler.setValid(valid)
        }

        fun getAllFilters(): List<AbstractFilter> = this@NotificationListener.getAllFilters()

        fun setOnFilterChanged(callbackObject: NotificationFilterHandler.OnFiltersChanged) {
            filterHandler.setOnFiltersChanged(callbackObject)
        }

        fun flushActiveNotificationForRepository() {
            pushNotificationToRepository()
        }

    }

    private val filterHandler = NotificationFilterHandler()

    private fun cancelOneNotification(key: String) {
        super.cancelNotification(key)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        LogUtil.v(
            TAG,
            "onListenerConnected: NotificationListener be connected to notification manager"
        )
        iniNotificationFilterHandler()
        pushNotificationToRepository()
    }

    private fun pushNotificationToRepository() {
        val list = activeNotifications.toList()
        LogUtil.d(TAG,"FilterHandler: ${filterHandler.size()} filters")
        Repository.loadActiveNotification(list.map {
            it.toNotificationInfo()
        }.filter(filterHandler::check))
        for (item in list) {
            LogUtil.d(TAG, item.toNotificationInfo().toString() + list.size.toString())
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        LogUtil.d(TAG, "onBind")
        val action = intent?.action
        //reference: https://stackoverflow.com/questions/34625022/android-service-not-yet-bound-but-onbind-is-called
        return if (SERVICE_INTERFACE == action) {
            LogUtil.d(TAG, "bound by system")
            super.onBind(intent)
        } else {
            LogUtil.d(TAG, "bound by application")
            mBinder
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        LogUtil.d(TAG, "onNotificationPosted: ${sbn?.packageName}")
        val notification = sbn?.toNotificationInfo() ?: return
        if (filterHandler.check(notification)) {
            Repository.addActiveNotification(notification)
        } else {
            return
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        LogUtil.d(TAG, "onNotificationRemoved: id=${sbn?.id}")
        Repository.removeActiveNotification(sbn?.id?.toLong())
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.d(TAG, "onCreate: service be created")
        useForeground()
    }

    private fun useForeground() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "NotificationDrawer",
            "Foreground Service",
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)

        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val notification = NotificationCompat.Builder(this, "NotificationDrawer")
            .setContentTitle("NotificationDrawer")
            .setContentText("NotificationListener is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_launcher_foreground
                )
            )
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d(TAG, "onDestroy: service be destroyed")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.d(TAG, "onStartCommand")
        toggleNotificationListenerService()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun toggleNotificationListenerService() {
        val pm = packageManager
        pm.setComponentEnabledSetting(
            ComponentName(
                this,
                NotificationListener::class.java
            ),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName(
                this,
                NotificationListener::class.java
            ),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }

    private fun iniNotificationFilterHandler() {
        val allFilter = Repository.getFilters()
        filterHandler.addAllFilter(allFilter)
        val valid =
            PreferenceManager.getDefaultSharedPreferences(this).getBoolean("use_filter", false)
        filterHandler.setValid(valid)
    }

    fun getAllFilters(): List<AbstractFilter> {
        return filterHandler.getAllFilters()
    }

}