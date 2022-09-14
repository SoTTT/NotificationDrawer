package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.data.defined.NotificationInfo
import java.util.*

class NotificationFilterHandler : FilterCollection, Checkable {

    private val mFilterCollection = mutableListOf<AbstractFilter>()

    private var _isValid: Boolean = true

    val isValid: Boolean get() = _isValid

    fun setValid(valid: Boolean) {
        _isValid = valid
    }

    override fun check(notification: NotificationInfo): Boolean {
        var flag = true
        mFilterCollection.forEach {
            flag = it.check(notification)
        }
        return flag
    }

    override fun addFilter(filter: AbstractFilter): Boolean {
        return mFilterCollection.add(filter)
    }

    override fun removeFilter(tag: String): Boolean {
        return mFilterCollection.removeIf {
            it.tag == tag
        }
    }

    override fun addAllFilter(list: List<AbstractFilter>) {
        for (item in list) {
            addFilter(item)
        }
    }

    override fun filter(notifications: Collection<NotificationInfo>): Collection<NotificationInfo> {
        return notifications.filter {
            check(it)
        }
    }
}