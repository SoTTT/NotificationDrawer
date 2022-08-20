package com.sottt.notificationdrawer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.sottt.notificationdrawer.DAO.Repository
import com.sottt.notificationdrawer.MainActivity
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.Util.LogUtil
import com.sottt.notificationdrawer.Util.toNotificationInfo
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import java.lang.Thread.sleep
import kotlin.concurrent.thread


class NotificationListener : NotificationListenerService() {

    companion object {
        const val TAG = "NotificationListener_SOTTT"
    }

    private val mBinder = NotificationListenBinder()

    inner class NotificationListenBinder : Binder() {

        fun getNotification() = activeNotifications.toList()

    }


    override fun onListenerConnected() {
        super.onListenerConnected()
        LogUtil.d(
            TAG,
            "onListenerConnected: NotificationListener be connected to notification manager"
        )
        val list = activeNotifications.toList()
        Repository.loadActiveNotification(list.map {
            it.toNotificationInfo()
        })
        for (item in list) {
            LogUtil.d(TAG, item.toNotificationInfo().toString() + list.size.toString())
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
//        thread {
//            sleep(5000)
//            val list = activeNotifications.toList()
//            Repository.loadActiveNotification(list.map {
//                it.toNotificationInfo()
//            })
//            for (item in list) {
//                LogUtil.d(TAG, item.toNotificationInfo().toString() + list.size.toString())
//            }
//        }
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
        val list = activeNotifications.toList()
        LogUtil.d(TAG, "onNotificationPosted: size of activeNotification is ${list.size}")
        Repository.addActiveNotification(
            sbn?.toNotificationInfo() ?: NotificationInfo(
                "null",
                "null",
                "null"
            )
        )
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        LogUtil.d(TAG, "onNotificationRemoved: ")
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
        return super.onStartCommand(intent, flags, startId)
    }

}