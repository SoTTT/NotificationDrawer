package com.sottt.notificationdrawer.ui.welcomeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sottt.notificationdrawer.Util.notificationAccessEnable
import com.sottt.notificationdrawer.Util.notificationEnable

class WelcomeFragmentViewModel : ViewModel() {

    private val _isHaveNotificationAccessPermission = MutableLiveData<Boolean>().apply {
        value = notificationAccessEnable()
    }

    val isHaveNotificationAccessPermission: LiveData<Boolean> = _isHaveNotificationAccessPermission

    private val _isAllowedToNotify = MutableLiveData<Boolean>().apply {
        value = notificationEnable()
    }

    val isAllowedToNotify: LiveData<Boolean> = _isAllowedToNotify

    fun flushAccessNotificationPermission() {
        _isHaveNotificationAccessPermission.value = notificationAccessEnable()
    }

    fun flushNotificationPermission() {
        _isAllowedToNotify.value = notificationEnable()
    }

    fun flushPermission() {
        flushAccessNotificationPermission()
        flushNotificationPermission()
    }
}