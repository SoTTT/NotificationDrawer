package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.data.defined.NotificationInfo
import java.util.*

class NotificationFilterHandler : FilterCollection, Checkable {

    interface OnFiltersChanged {
        fun onFilterRemoved(filter: AbstractFilter)
        fun onFilterAdded(filter: AbstractFilter)
    }

    private val mFilterCollection = mutableListOf<AbstractFilter>()

    private var onFiltersChangedCallback: OnFiltersChanged? = null

    private var _isValid: Boolean = true

    val isValid: Boolean get() = _isValid

    fun setValid(valid: Boolean) {
        _isValid = valid
    }

    override fun check(notification: NotificationInfo): Boolean {
        return if (isValid) {
            var flag = true
            mFilterCollection.forEach {
                flag = it.check(notification)
            }
            flag
        } else {
            true
        }

    }

    override fun addFilter(filter: AbstractFilter): Boolean {
        onFiltersChangedCallback?.onFilterAdded(filter)
        return mFilterCollection.add(filter)
    }

    override fun removeFilter(tag: String): Boolean {
        val filter = mFilterCollection.find {
            it.tag == tag
        }
        return if (filter == null) {
            false
        } else {
            onFiltersChangedCallback?.onFilterRemoved(filter)
            mFilterCollection.remove(filter)
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
        for (item in mFilterCollection) {
            onFiltersChangedCallback?.onFilterRemoved(item)
        }
        mFilterCollection.clear()
    }

    override fun filter(notifications: Collection<NotificationInfo>): Collection<NotificationInfo> {
        return if (isValid) {
            notifications.filter {
                check(it)
            }
        } else {
            notifications
        }
    }

    fun getAllFilters(): List<AbstractFilter> {
        return mFilterCollection
    }

    fun setOnFiltersChanged(callbackObject: OnFiltersChanged) {
        onFiltersChangedCallback = callbackObject
    }
}