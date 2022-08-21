package com.sottt.notificationdrawer.ui.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sottt.notificationdrawer.data.defined.NotificationInfo

class HomeFragmentViewModel : ViewModel() {

    private val _adapterData = MutableLiveData<List<NotificationInfo>>()

    val adapterData = _adapterData

    var newStatus: Int? = null

    fun flushNotification() {

    }

    fun setCurrentNotification(list: List<NotificationInfo>) {
        _adapterData.value = list
    }

}