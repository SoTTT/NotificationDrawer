package com.sottt.notificationdrawer.filter

import com.sottt.notificationdrawer.data.defined.NotificationInfo

class PackageFilter() : AbstractFilter() {

    init {
        tag = "PACKAGE_FILTER"
        valid = true
    }

    private val packageNameSet = HashSet<String>()

    override fun check(notification: NotificationInfo): Boolean {
        return !packageNameSet.contains(notification.packageName)
    }

    fun addPackageName(packageName: String): Boolean {
        return packageNameSet.add(packageName)
    }

    fun removePackageName(packageName: String): Boolean {
        return packageNameSet.remove(packageName)
    }

}