package com.sottt.notificationdrawer.DAO

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.edit
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationContext
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationSettings
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import kotlin.concurrent.thread

object Repository {

    private val dao by lazy {
        NotificationDatabase.getDatabase(applicationContext()).NotificationDao()
    }

    fun readSettings() {

    }

    @SuppressLint("CommitPrefEdits")
    fun writeSettings() {
        val settingsEditor =
            applicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
    }

    fun storeAllNotification(notification: List<NotificationInfo>) {
        for (item in notification) {
            storeNotification(item)
        }
    }

    fun storeNotification(notification: NotificationInfo) {
        thread {
            notification.id = dao.insertNotification(notification)
        }
    }

    fun loadAllNotificationRecord(): List<NotificationInfo> = dao.loadAllNotificationRecord()

}