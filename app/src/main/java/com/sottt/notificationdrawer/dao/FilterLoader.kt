package com.sottt.notificationdrawer.dao

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sottt.notificationdrawer.NotificationDrawerApplication
import com.sottt.notificationdrawer.filter.AbstractFilter
import kotlinx.coroutines.sync.Mutex
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class FilterLoader {

    class NegativeLengthException : Exception() {
        override val message: String
            get() = "the length of a array or list not be negative"
    }

    class KeyNotFoundException(private val index: Int) : Exception() {
        override val message: String
            get() = "the $index key si not found"
    }

    private val lock = Mutex()

    private val uuidArray = ArrayList<String>()

    private val filterStore by lazy {
        NotificationDrawerApplication.applicationContext()
            .getSharedPreferences("filter", Context.MODE_PRIVATE)
    }

    companion object {
        const val FILTER_COUNT = "FILTER_COUNT"
    }


    fun storeFilters(filters: List<AbstractFilter>) {
        thread {
            synchronized(lock) {
                for (item in filters) {
                    storeFilter(item, item.javaClass)
                }
                filterStore.edit().apply {
                    val filerCount = uuidArray.size
                    putInt(FILTER_COUNT, filerCount)
                    for (index in 0 until filerCount) {
                        putString("FILTER_UUID_$index", uuidArray.elementAt(index))
                    }
                }.apply()
            }
        }
    }

    private fun <T> storeFilter(filter: AbstractFilter, javaClass: Class<T>) {
        filterStore.edit().apply {
            val json = Gson().toJson(filter, javaClass)
            val uuid = UUID.randomUUID().toString()
            uuidArray.add(uuid)
            putString(uuid, json)
            putString(uuid + "class", javaClass.name)
        }.apply()
    }

    fun storeFilter(filter: AbstractFilter) {
        synchronized(lock) {
            storeFilter(filter, filter.javaClass)
            filterStore.edit().apply {
                putInt(FILTER_COUNT, uuidArray.size)
                putString(
                    "FILTER_UUID_${uuidArray.size - 1}",
                    uuidArray.elementAt(uuidArray.size - 1)
                )
            }.apply()
        }
    }

    fun loadFilters(): List<AbstractFilter> {
        val filterCount = filterStore.getInt(FILTER_COUNT, 0)
        if (filterCount < 0) {
            throw NegativeLengthException()
        }
        if (filterCount == 0) {
            return listOf()
        } else {
            val classList = ArrayList<String>()
            val uuidArray = ArrayList<String>()
            for (index in 0 until filterCount) {
                val uuid = filterStore.getString("FILTER_UUID_$index", "")
                if (uuid == null || uuid.isEmpty()) {
                    throw KeyNotFoundException(index)
                } else {
                    uuidArray.add(uuid)
                    val className = filterStore.getString(uuid + "class", "")
                    if (className == null || className.isEmpty()) {
                        throw KeyNotFoundException(index)
                    } else {
                        classList.add(className)
                    }
                }
            }
            return uuidArray.map {
                val index = uuidArray.indexOf(it)
                val json = filterStore.getString(it, "")
                val className = classList.elementAt(index)
//                val gson=GsonBuilder().registerTypeAdapter()
                Gson().fromJson(json, Class.forName(className)) as AbstractFilter
            }
        }
    }

}