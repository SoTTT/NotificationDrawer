package com.sottt.notificationdrawer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class NotificationDrawerApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var mContext: Context

        fun applicationContext() = mContext

    }


    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
    }

}