package com.sottt.notificationdrawer.data.defined

data class ApplicationPermissionStatus(
    var notificationPushPermission: Boolean,
    var notificationAccessPermission: Boolean,
    var ignorePowerOptimization: Boolean
)
