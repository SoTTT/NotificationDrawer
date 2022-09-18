package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.data.defined.NotificationInfo

interface FilterCollection {

    fun filter(notifications: Collection<NotificationInfo>): Collection<NotificationInfo>

    fun addFilter(filter: AbstractFilter): Boolean

    fun removeFilter(tag: String): Boolean

    fun addAllFilter(list: List<AbstractFilter>)

    fun isEmpty(): Boolean

    fun clear()

    fun size(): Int
}