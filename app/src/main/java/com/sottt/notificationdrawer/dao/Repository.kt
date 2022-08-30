package com.sottt.notificationdrawer.dao

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationContext
import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import com.sottt.notificationdrawer.filter.AbstractFilter
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import kotlin.concurrent.thread

object Repository {

    data class PackageNameAndCount(val name: String?, val count: Int?)

    private const val TAG = "Repository"

    //_activeNotification应当在自己发生变化时将变化同步给HoneFragmentViewModel的LiveData
    private var _activeNotification = MutableLiveData<List<NotificationInfo>>()

    private val filterStore by lazy {
        applicationContext().getSharedPreferences("filter", Context.MODE_PRIVATE).edit()
    }

    val activeNotification: LiveData<List<NotificationInfo>> = _activeNotification

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

    fun readSettings() {

    }

    fun create() {}

    @SuppressLint("CommitPrefEdits")
    fun writeSettings() {
        val settingsEditor =
            applicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
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
        thread {
            for (item in filters) {
                storeFilter(item, item.javaClass)
            }
        }
    }

    private fun <T> storeFilter(filter: AbstractFilter, javaClass: Class<T>) {
        filterStore.apply {
            val json = Gson().toJson(filter, javaClass)
            putString(UUID.randomUUID().toString(), json)
        }.apply()
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
                dao.selectWithPackageName(packageName)
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

}