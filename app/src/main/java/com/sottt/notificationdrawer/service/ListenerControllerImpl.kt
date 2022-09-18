package com.sottt.notificationdrawer.service

import com.sottt.notificationdrawer.filter.AbstractFilter

class ListenerControllerImpl {

    private lateinit var notificationListenerBinder: NotificationListener.NotificationListenBinder

    fun setBinder(binder: NotificationListener.NotificationListenBinder) {
        notificationListenerBinder = binder
    }

    fun getAllFilters(): List<AbstractFilter> = notificationListenerBinder.getAllFilters()

}