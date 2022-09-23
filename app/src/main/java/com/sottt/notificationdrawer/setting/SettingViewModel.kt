package com.sottt.notificationdrawer.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sottt.notificationdrawer.data.defined.FilterInfo
import com.sottt.notificationdrawer.service.ListenerController

class SettingViewModel() : ViewModel() {

    init {
        loadFilters()
    }

    private val _filterInfo = MutableLiveData<List<FilterInfo>>()
//    private val _filters = MutableLiveData<MutableList<AbstractFilter>>()

    val filterInfo: LiveData<List<FilterInfo>> = _filterInfo

    private fun loadFilters() {
        _filterInfo.postValue(ListenerController.getAllFilterInfo())
    }

}