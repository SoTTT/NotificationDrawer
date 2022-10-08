package com.sottt.notificationdrawer.dao

import android.os.Parcelable
import com.google.gson.Gson
import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.filter.AbstractFilter
import com.sottt.notificationdrawer.filter.PackageFilter
import com.sottt.notificationdrawer.filter.PackageFilterData

class FilterConverter {

    private val filterTagToFilterPackageName = HashMap<String, String?>().apply {
        put("PACKAGE_FILTER", PackageFilter::class.java.name)
    }
    private val filterTagToFilterDataClassName = HashMap<String, String?>().apply {
        put("PACKAGE_FILTER", PackageFilterData::class.java.name)
    }

    companion object {

        const val TAG = "FilterConverter"

        fun toData(filter: PackageFilter) =
            PackageFilterData(filter.tag, filter.valid, filter.name, filter.getPackageNameArray())
    }

    fun <T, V> registerFilterTag(
        tag: String,
        classObject: Class<T>,
        dataClassObject: Class<V>
    ): Boolean {
        val old = filterTagToFilterPackageName.put(tag, classObject.name)
        filterTagToFilterDataClassName[tag] = dataClassObject.name
        return old == null
    }

    fun deleteRegisterTag(tag: String): Boolean {
        val old = filterTagToFilterPackageName.put(tag, null)
        filterTagToFilterDataClassName[tag] = null
        return old != null
    }

    fun registeredTag(): Set<String> {
        return filterTagToFilterPackageName.keys;
    }

    fun <T> convertFilterToData(
        filter: AbstractFilter, dataClassObject: Class<T>
    ): Parcelable {
        return when (filterTagToFilterPackageName[filter.tag] ?: "null") {
            PackageFilter::javaClass.name -> {
                val constructor = dataClassObject.getConstructor(
                    String::class.java,
                    Boolean::class.java,
                    String::class.java,
                    List::class.java
                )
                if (filter is PackageFilter) {
                    constructor.newInstance(
                        filter.tag,
                        filter.valid,
                        filter.name,
                        filter.getPackageNameArray()
                    ) as Parcelable
                } else {
                    throw Exception()
                }
            }
            else -> {
                throw Exception()
            }
        }
    }

    fun converterJsonToFilter(json: String, tag: String): AbstractFilter? {
        return if (filterTagToFilterPackageName.keys.contains(tag)) {
            val data = Gson().fromJson(json, Class.forName(filterTagToFilterDataClassName[tag]!!))
            Util.LogUtil.d(TAG, data.toString())
            when (tag) {
                "PACKAGE_FILTER" -> {
                    PackageFilter().apply {
                        addPackageName((data as PackageFilterData).packageNameList ?: listOf())
                        name = data.name
                        valid = data.valid
                    }
                }
                else -> {
                    throw Exception()
                }
            }
        } else {
            Util.LogUtil.w(TAG, "the filter tag $tag is not registered, please check it")
            null
        }
    }
}