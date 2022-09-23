package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.data.defined.NotificationInfo

class FilterGroup() : FilterCollection, AbstractFilter() {

    override fun filter(notifications: Collection<NotificationInfo>): Collection<NotificationInfo> {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }

    override fun addFilter(filter: AbstractFilter): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeFilter(tag: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun check(notification: NotificationInfo): Boolean {
        TODO("Not yet implemented")
    }

    override fun addAllFilter(list: List<AbstractFilter>) {
        TODO("Not yet implemented")
    }
}