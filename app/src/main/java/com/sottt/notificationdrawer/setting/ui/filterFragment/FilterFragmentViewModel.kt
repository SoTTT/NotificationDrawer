package com.sottt.notificationdrawer.setting.ui.filterFragment

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel

class FilterFragmentViewModel : ViewModel() {

    private val filterCache = HashMap<String, Int>()

    fun findViewByKey(key: String, root: View): ViewGroup? {
        if (filterCache[key] == null) {
            return null
        }
        return root.findViewById(filterCache[key]!!)
    }

    fun putView(key: String, view: View): Boolean {
        return if (filterCache[key] != null) {
            false
        } else {
            filterCache[key] = view.id;
            true
        }
    }

}