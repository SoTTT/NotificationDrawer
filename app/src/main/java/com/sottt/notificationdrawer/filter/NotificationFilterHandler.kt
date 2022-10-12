package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.data.defined.NotificationInfo

class NotificationFilterHandler : FilterCollection, Checkable {

    init {
        Repository.setOnFilterChanged(object : Repository.OnFilterChanged {
            override fun onFilterRemoved(filter: AbstractFilter): Boolean {
                return this@NotificationFilterHandler.removeFilter(filter.key)
            }

            override fun onFilterAdded(filter: AbstractFilter): Boolean {
                return this@NotificationFilterHandler.addFilter(filter)
            }
        })
    }

    /**
     * the onFilterRemoved and onFilterAdded will be invoked before a filter removed or added to handler as callback functions
     */
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
        val flag = mFilterCollection.add(filter)
        onFiltersChangedCallback?.onFilterAdded(filter)
        return flag
    }

    override fun removeFilter(key: String): Boolean {
        val filter = mFilterCollection.find {
            it.key == key
        }
        return if (filter == null) {
            false
        } else {
            val flag = mFilterCollection.remove(filter)
            onFiltersChangedCallback?.onFilterRemoved(filter)
            flag
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