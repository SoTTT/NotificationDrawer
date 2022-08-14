package com.sottt.notificationdrawer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.sottt.notificationdrawer.MainActivity
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.Util.LogUtil

const val TAG = "NotificationListener_SOTTT"

class NotificationListener : NotificationListenerService() {

    class NotificationListenBinder : Binder() {

    }


    override fun onListenerConnected() {
        super.onListenerConnected()
        LogUtil.d(
            TAG,
            "onListenerConnected: NotificationListener be connected to notification manager"
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        LogUtil.d(TAG, "onBind")
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        LogUtil.d(TAG, "onNotificationPosted: ${sbn?.packageName}")

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        LogUtil.d(TAG, "onNotificationRemoved: ")
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.d(TAG, "onCreate: service be created")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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