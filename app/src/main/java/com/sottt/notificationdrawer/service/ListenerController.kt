package com.sottt.notificationdrawer.service

import com.sottt.notificationdrawer.filter.AbstractFilter

object ListenerController {

    interface ControllerInitStatusChanged {
        fun changed(new: Boolean)
    }

    class BinderNotConnectedException() : Exception() {
        override val message: String
            get() = "service not connect"
    }

    private var mIsInit = false

    private val listenerControllerImpl = ListenerControllerImpl()

    val isInit get() = mIsInit

    fun setBinder(binder: NotificationListener.NotificationListenBinder) {
        if (!isInit) {
            listenerControllerImpl.setBinder(binder)
            mIsInit = true
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

}