package com.sottt.notificationdrawer.service

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.sottt.notificationdrawer.Util.LogUtil

const val TAG = "NotificationListener_SOTTT"

class NotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        LogUtil.v(TAG, "onListenerConnected: NotificationListener be connected to notification manager")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        LogUtil.v(TAG, "onNotificationPosted: ${sbn?.packageName}")
        
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        LogUtil.v(TAG, "onNotificationRemoved: ")
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.v(TAG, "onCreate: service be created")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.v(TAG, "onDestroy: service be destroyed")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

}