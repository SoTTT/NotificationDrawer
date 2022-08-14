package com.sottt.notificationdrawer.ui.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sottt.notificationdrawer.data.defined.NotificationInfo

class HomeFragmentViewModel : ViewModel() {

    private val _adapterData = MutableLiveData<List<NotificationInfo>>().apply {
        TODO("7.24")
    }

    val adapterData = _adapterData

    fun flushNotification() {
        TODO("7.24")
    }

}