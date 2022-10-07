package com.sottt.notificationdrawer.dao

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sottt.notificationdrawer.NotificationDrawerApplication
import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.filter.AbstractFilter
import com.sottt.notificationdrawer.filter.PackageFilter
import com.sottt.notificationdrawer.filter.PackageFilterData
import kotlinx.coroutines.sync.Mutex
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.streams.toList

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

    private val filterStore by lazy {
        NotificationDrawerApplication.applicationContext()
            .getSharedPreferences("filter", Context.MODE_PRIVATE)
    }

    private val converter = FilterConverter()

    companion object {
        const val FILTER_COUNT = "FILTER_COUNT"
        const val TAG = "FilterLoader"
    }

    fun storeFilters(filters: List<AbstractFilter>) {
        filterStore.edit().apply {
            filters.groupBy {
                it.tag
            }.apply {
                for ((key, list) in this) {
                    putStringSet(key, list.map {
                        Gson().toJson(it)
                    }.toSet())
                }
            }
        }.apply()
    }

    private fun <T> storeFilter(filter: AbstractFilter, javaClass: Class<T>) {

    }

    fun storeFilter(filter: AbstractFilter) {

    }

    fun loadFilters(): List<AbstractFilter> {
        val allTag = converter.registeredTag()
        val filters = mutableListOf<AbstractFilter?>()
        filterStore.apply {
            for (tag in allTag) {
                filters.addAll((getStringSet(tag, setOf()) ?: setOf()).stream().map {
                    converter.converterJsonToFilter(it, tag)
                }.toList())
            }
        }
        return mutableListOf<AbstractFilter>().apply {
            for (filter in filters) {
                if (filter != null) {
                    add(filter)
                }
            }
        }
    }

}