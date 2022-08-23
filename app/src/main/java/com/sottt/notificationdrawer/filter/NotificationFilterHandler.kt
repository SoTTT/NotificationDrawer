package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.data.defined.NotificationInfo

class NotificationFilterHandler : Checkable {

    private val mFilterCollection = mutableListOf<AbstractFilter>()

    override fun check(notification: NotificationInfo): Boolean {
        var flag = true
        mFilterCollection.forEach {
            flag = it.check(notification)
        }
        return flag
    }

    fun addFilter(filter: AbstractFilter) {
        mFilterCollection.add(filter)
    }

    fun removeFilter(tag: String) {
        mFilterCollection.removeIf {
            it.tag == tag
        }
    }

    fun filter(notifications: List<NotificationInfo>): List<NotificationInfo> {
        return notifications.filter {
            check(it)
        }
    }
}