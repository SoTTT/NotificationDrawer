package com.sottt.notificationdrawer.DAO

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.edit
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationContext
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationSettings

object Repository {

    fun readSettings() {

    }

    @SuppressLint("CommitPrefEdits")
    fun writeSettings() {
        val settingsEditor =
            applicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
    }

}