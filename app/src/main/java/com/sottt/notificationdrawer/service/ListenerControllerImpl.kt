package com.sottt.notificationdrawer.service

import com.sottt.notificationdrawer.filter.AbstractFilter
import com.sottt.notificationdrawer.filter.NotificationFilterHandler

class ListenerControllerImpl {

    private lateinit var notificationListenerBinder: NotificationListener.NotificationListenBinder

    fun setBinder(binder: NotificationListener.NotificationListenBinder) {
        notificationListenerBinder = binder
    }

    fun getAllFilters(): List<AbstractFilter> = notificationListenerBinder.getAllFilters()

    fun setOnFilterChanged(callbackObject: NotificationFilterHandler.OnFiltersChanged) {
        notificationListenerBinder.setOnFilterChanged(callbackObject)
    }

    fun flushActiveNotificationForRepository() {
        notificationListenerBinder.flushActiveNotificationForRepository()
    }

}