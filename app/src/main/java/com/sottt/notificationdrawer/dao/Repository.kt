package com.sottt.notificationdrawer.dao

import com.sottt.notificationdrawer.NotificationDrawerApplication
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationContext
import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.data.defined.ApplicationPermissionStatus
import com.sottt.notificationdrawer.data.defined.ApplicationSettings
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import com.sottt.notificationdrawer.filter.AbstractFilter
import com.sottt.notificationdrawer.filter.NotificationFilterHandler
import com.sottt.notificationdrawer.service.ListenerController
import com.sottt.notificationdrawer.setting.ui.AppSettingsFragment
import java.util.LinkedList
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask
import kotlin.concurrent.thread

object Repository {

    private const val TAG = "Repository"

    enum class NotificationCategory {
        GENERAL_NOTIFICATION,
        FILTERED_NOTIFICATION,
    }

    data class PackageNameAndCount(val name: String?, val count: Int?)

    interface OnFilterChanged {
        fun onFilterRemoved(filter: AbstractFilter): Boolean
        fun onFilterAdded(filter: AbstractFilter): Boolean
    }

    interface OnActiveNotificationChanged {
        fun onActiveNotificationAdded(
            notification: NotificationInfo,
            category: NotificationCategory
        )

        fun onActiveNotificationRemoved(
            notification: NotificationInfo,
            category: NotificationCategory
        )
    }

    init {
        if (ListenerController.isInit) {
            ListenerController.setOnFilterChanged(object :
                NotificationFilterHandler.OnFiltersChanged {
                override fun onFilterRemoved(filter: AbstractFilter) {
                    Util.LogUtil.d(TAG, "a filter removed, flush active notification list")
                    ListenerController.flushActiveNotificationForRepository()
                }

                override fun onFilterAdded(filter: AbstractFilter) {
                    Util.LogUtil.d(TAG, "a filter added, flush active notification list")
                    ListenerController.flushActiveNotificationForRepository()
                }
            })
            Util.LogUtil.d(TAG, "Controller is init! callback is registered")
        } else {
            ListenerController.setOnControllerInitStatusChanged(object :
                ListenerController.OnControllerInitStatusChanged {
                override fun changed(new: Boolean) {
                    Util.LogUtil.d(TAG, "Controller isn't init! register StatusChangedCallback")
                    if (new)
                        ListenerController.setOnFilterChanged(object :
                            NotificationFilterHandler.OnFiltersChanged {
                            override fun onFilterRemoved(filter: AbstractFilter) {
                                Util.LogUtil.d(
                                    TAG,
                                    "a filter removed, flush active notification list"
                                )
                                ListenerController.flushActiveNotificationForRepository()
                            }

                            override fun onFilterAdded(filter: AbstractFilter) {
                                Util.LogUtil.d(
                                    TAG,
                                    "a filter added, flush active notification list"
                                )
                                ListenerController.flushActiveNotificationForRepository()
                            }
                        })
                    Util.LogUtil.d(TAG, "Controller is init! callback is registered")
                }
            })
        }
    }

    private val settingsPreference by lazy {
        AppSettingsFragment.getPreference(applicationContext())
    }

    private var activeNotification: MutableList<NotificationInfo> = LinkedList<NotificationInfo>()
    val activeNotificationList get() = activeNotification

    private var filteredNotification = mutableListOf<NotificationInfo>()

    private val filterLoader by lazy {
        FilterLoader()
    }

    private val filterList = filterLoader.loadFilters().toMutableList()

    private var filterChangedCallbackObject = HashSet<OnFilterChanged>()
    private var notificationChangedCallbackObject = HashSet<OnActiveNotificationChanged>()

    fun loadActiveNotification(list: List<NotificationInfo>) {
        for (item in list) {
            addActiveNotification(item)
        }
        storeAllNotification(list)
    }

    fun addActiveNotification(item: NotificationInfo) {
        activeNotification.add(item)
        callbackForNotificationAdded(item, NotificationCategory.GENERAL_NOTIFICATION)
        storeNotification(item)
    }

    fun removeActiveNotification(id: Int?): Boolean {
        if (id == null) {
            Util.LogUtil.d(TAG, "${TAG}: notification id is null")
            return false
        }
        return activeNotification.removeIf {
            if (it.id == id.toInt()) {
                callbackForNotificationRemoved(it, NotificationCategory.GENERAL_NOTIFICATION)
                Util.LogUtil.d(TAG, "the found id :$id")
                true
            } else {
                Util.LogUtil.d(TAG, "not found id :$id")
                false
            }
        }
    }

    private val dao by lazy {
        NotificationDatabase.getDatabase(applicationContext()).NotificationDao()
    }

    fun readSettings(): ApplicationSettings {
        val applicationPermissionStatus =
            ApplicationPermissionStatus(
                settingsPreference.getBoolean("permission_push_notification", false),
                settingsPreference.getBoolean("permission_access_notification", false),
                settingsPreference.getBoolean("permission_ignore_battery_optimizations", false)
            )
        return ApplicationSettings(applicationPermissionStatus)

    }


    fun writeSettings(settings: ApplicationSettings) {
        settingsPreference.edit().apply {
            putBoolean(
                "permission_access_notification",
                settings.permissionStatus.notificationAccessPermission
            )
            putBoolean(
                "permission_push_notification",
                settings.permissionStatus.notificationPushPermission
            )
            putBoolean(
                "permission_ignore_battery_optimizations",
                settings.permissionStatus.ignorePowerOptimization
            )
        }.apply()
    }

    private fun storeAllNotification(notification: List<NotificationInfo>) {
        for (item in notification) {
            storeNotification(item)
        }
    }

    private fun storeNotification(notification: NotificationInfo) {
        thread {
            synchronized(notification) {
                val keys = dao.selectAllKey()
                if (!keys.contains(notification.key))
                    notification.dataBaseId = dao.insertNotification(notification)
                Util.LogUtil.v(
                    TAG,
                    "DAO(Thread: ${Thread.currentThread().id}) :${notification.toString()}"
                )
            }
        }
    }

    fun loadAllNotificationRecord(): FutureTask<List<NotificationInfo>> {
        val callable = Callable {
            synchronized(this) {
                dao.selectAllNotificationRecord()
            }
        }
        val task = FutureTask(callable)
        Thread(task).start()
        return task
    }


    private fun <T> storeFilter(filter: AbstractFilter, javaClass: Class<T>) {
        filterLoader.storeFilter(filter)
    }

    fun getNotificationCount(): FutureTask<Int> {
        val callable = Callable {
            synchronized(this) {
                dao.count()
            }
        }
        val task = FutureTask(callable)
        Thread(task).start()
        return task
    }

    fun getNotificationRecordWithPackageName(packageName: String): FutureTask<List<NotificationInfo>> {
        val callable = Callable {
            synchronized(this) {
                val list = dao.selectWithPackageName(packageName)
                for (item in list) {
                    item.smallIcon = NotificationDrawerApplication.getAppIcon(item.packageName)!!
                }
                list
            }
        }
        val task = FutureTask(callable)
        Thread(task).start()
        return task
    }

    fun getPackageNameAndCount(): FutureTask<List<PackageNameAndCount>> {
        val callable = Callable {
            synchronized(this) {
                dao.selectAllPackageNameAndCount()
            }
        }
        val task = FutureTask(callable)
        Thread(task).start()
        return task
    }

    fun getFilters(): List<AbstractFilter> {
        return filterList.toList()
    }

    private fun storeFilter(filters: List<AbstractFilter>) {
        filterLoader.storeFilters(filters)
    }

    fun storeAllFilter() {
        storeFilter(filterList)
    }

    fun addFilter(filter: AbstractFilter): Boolean {
        filterLoader.storeFilter(filter)
        val flag = filterList.add(filter)
        callbackForFilterAdded(filter)
        return flag
    }

    fun addFilter(filter: Collection<AbstractFilter>): Boolean {
        return filterList.addAll(filter)
    }

    fun setOnFilterChanged(callbackObject: OnFilterChanged):Boolean {
        return this.filterChangedCallbackObject.add(callbackObject)
    }

    fun setOnActiveNotificationChanged(callbackObject: OnActiveNotificationChanged):Boolean {
        return this.notificationChangedCallbackObject.add(callbackObject)
    }

    private fun callbackForFilterAdded(filter: AbstractFilter) {
        filterChangedCallbackObject.forEach {
            it.onFilterAdded(filter)
        }
    }

    private fun callbackForFilterRemoved(filter: AbstractFilter) {
        filterChangedCallbackObject.forEach {
            it.onFilterRemoved(filter)
        }
    }

    private fun callbackForNotificationAdded(
        notification: NotificationInfo,
        category: NotificationCategory
    ) {
        notificationChangedCallbackObject.forEach {
            it.onActiveNotificationAdded(notification, category)
        }
    }

    private fun callbackForNotificationRemoved(
        notification: NotificationInfo,
        category: NotificationCategory
    ) {
        notificationChangedCallbackObject.forEach {
            it.onActiveNotificationRemoved(notification, category)
        }
    }

    fun addFilteredNotification(info: NotificationInfo) {
        if (!filteredNotification.add(info)) {
            return;
        }
        storeNotification(info)
        callbackForNotificationAdded(info, NotificationCategory.FILTERED_NOTIFICATION)
    }

    fun removeFilterNotification(info: NotificationInfo) {
        if (!filteredNotification.removeIf {
                info.key == it.key
            }) {
            return;
        }
        callbackForNotificationRemoved(info, NotificationCategory.FILTERED_NOTIFICATION)
    }

}