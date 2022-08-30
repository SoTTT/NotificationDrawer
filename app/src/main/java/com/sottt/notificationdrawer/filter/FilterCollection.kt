package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.data.defined.NotificationInfo

interface FilterCollection : Checkable {

    fun filter(notifications: Collection<NotificationInfo>): Collection<NotificationInfo>

    fun addFilter(filter: AbstractFilter): Boolean

    fun removeFilter(tag: String): Boolean
}