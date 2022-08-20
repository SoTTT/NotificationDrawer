package com.sottt.notificationdrawer.DAO

import android.annotation.SuppressLint
import android.content.Context
import android.service.notification.StatusBarNotification
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationContext
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationSettings
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import kotlin.concurrent.thread

object Repository {

    private var _activeNotification = MutableLiveData<List<NotificationInfo>>()

    val activeNotification: LiveData<List<NotificationInfo>> = _activeNotification

    fun loadActiveNotification(list: List<NotificationInfo>) {
        _activeNotification.postValue(list)
    }

    fun addActiveNotification(item: NotificationInfo) {
        val mutableList = activeNotification.value?.toMutableList()
        mutableList?.add(item)
        loadActiveNotification(mutableList?.toList()!!)
    }

    private val dao by lazy {
        NotificationDatabase.getDatabase(applicationContext()).NotificationDao()
    }

    fun readSettings() {

    }

    fun create() {}

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