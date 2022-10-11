package com.sottt.notificationdrawer.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.internal.SynchronizedObject
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask
import kotlin.concurrent.thread

object Repository {

    private const val TAG = "Repository"

    data class PackageNameAndCount(val name: String?, val count: Int?)

    interface OnFilterChanged {
        fun onFilterRemoved(filter: AbstractFilter)
        fun onFilterAdded(filter: AbstractFilter)
    }

    init {
        if (ListenerController.isInit) {
            ListenerController.setOnFilterChanged(object :
                NotificationFilterHandler.OnFiltersChanged {
                override fun onFilterRemoved(filter: AbstractFilter) {
                    ListenerController.flushActiveNotificationForRepository()
                }

                override fun onFilterAdded(filter: AbstractFilter) {
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
                                ListenerController.flushActiveNotificationForRepository()
                            }

                            override fun onFilterAdded(filter: AbstractFilter) {
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

    //_activeNotification应当在自己发生变化时将变化同步给HoneFragmentViewModel的LiveData
    private var _activeNotification = MutableLiveData<List<NotificationInfo>>()

    val activeNotification: LiveData<List<NotificationInfo>> = _activeNotification

    private val filterLoader by lazy {
        FilterLoader()
    }

    private val filterList = filterLoader.loadFilters().toMutableList()

    private var callbackObject: OnFilterChanged? = null

    fun loadActiveNotification(list: List<NotificationInfo>) {
        _activeNotification.postValue(list)
        storeAllNotification(list)
    }

    fun addActiveNotification(item: NotificationInfo) {
        val mutableList = activeNotification.value?.toMutableList()
        mutableList?.add(item)
        loadActiveNotification(mutableList?.toList() ?: listOf())
        storeNotification(item)
    }

    fun removeActiveNotification(id: Int?) {
        if (id == null) {
            Util.LogUtil.d(TAG, "${TAG}: notification id is null")
            return
        }
        val it = activeNotification.value?.find {
            it.notificationId == id
        }
        _activeNotification.value = activeNotification.value?.filter {
            it.notificationId != id
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

    fun create() {}

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

    fun storeAllNotification(notification: List<NotificationInfo>) {
        for (item in notification) {
            storeNotification(item)
        }
    }

    fun storeNotification(notification: NotificationInfo) {
        thread {
            synchronized(notification) {
                val keys = dao.selectAllKey()
                if (!keys.contains(notification.key))
                    notification.id = dao.insertNotification(notification)
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


    fun storeFilter(filters: List<AbstractFilter>) {
        filterLoader.storeFilters(filters)
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

    fun storeAllFilter() {
        storeFilter(filterList)
    }

    fun addFilter(filter: AbstractFilter): Boolean {
        return filterList.add(filter)
    }

    fun addFilter(filter: Collection<AbstractFilter>): Boolean {
        return filterList.addAll(filter)
    }

    fun setOnFilterChanged(callbackObject: OnFilterChanged) {
        this.callbackObject = callbackObject
    }

}