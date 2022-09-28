package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.data.defined.NotificationInfo

class FilterGroup() : FilterCollection, AbstractFilter() {

    private val mFilterCollection = mutableListOf<AbstractFilter>()

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

    override fun size(): Int = mFilterCollection.size

    override fun isEmpty(): Boolean {
        return mFilterCollection.isEmpty()
    }

    override fun clear() {
        mFilterCollection.clear()
    }

    override fun filter(notifications: Collection<NotificationInfo>): Collection<NotificationInfo> {
        return notifications.filter {
            check(it)
        }
    }

    fun getAllFilters(): List<AbstractFilter> {
        return mFilterCollection
    }
}