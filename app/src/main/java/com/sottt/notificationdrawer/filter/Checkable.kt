package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.data.defined.NotificationInfo

interface Checkable {

    fun check(notification: NotificationInfo): Boolean

}