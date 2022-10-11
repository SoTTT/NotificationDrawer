package com.sottt.notificationdrawer.service

import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.data.defined.FilterInfo
import com.sottt.notificationdrawer.filter.AbstractFilter
import com.sottt.notificationdrawer.filter.NotificationFilterHandler

object ListenerController {

    private const val TAG = "ListenerController"

    private fun AbstractFilter.toFilterInfo(): FilterInfo {
        return FilterInfo(this.name ?: "", this.tag)
    }

    interface OnControllerInitStatusChanged {
        fun changed(new: Boolean)
    }

    class BinderNotConnectedException() : Exception() {
        override val message: String
            get() = "service not connect"
    }

    private var mIsInit = false

    private val listenerControllerImpl = ListenerControllerImpl()

    private var callbackObject: OnControllerInitStatusChanged? = null

    val isInit get() = mIsInit

    fun setBinder(binder: NotificationListener.NotificationListenBinder) {
        if (!isInit) {
            listenerControllerImpl.setBinder(binder)
            mIsInit = true
            callbackObject?.changed(true)
            Util.LogUtil.d(TAG, "Controller is Init!")
        }
    }

    fun activityDestroy() {
        mIsInit = false
    }

    fun getAllFilter(): List<AbstractFilter> {
        if (!mIsInit) {
            throw BinderNotConnectedException()
        }
        return listenerControllerImpl.getAllFilters()
    }

    fun create(): ListenerController = this

    fun getAllFilterInfo(): List<FilterInfo> {
        return getAllFilter().map {
            it.toFilterInfo()
        }
    }

    fun setOnFilterChanged(callbackObject: NotificationFilterHandler.OnFiltersChanged) {
        listenerControllerImpl.setOnFilterChanged(callbackObject)
    }

    fun setOnControllerInitStatusChanged(callbackObject: OnControllerInitStatusChanged) {
        this.callbackObject = callbackObject
    }

    fun flushActiveNotificationForRepository() {
        listenerControllerImpl.flushActiveNotificationForRepository()
    }

}