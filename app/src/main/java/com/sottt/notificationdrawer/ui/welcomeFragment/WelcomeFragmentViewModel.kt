package com.sottt.notificationdrawer.ui.welcomeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sottt.notificationdrawer.Util

class WelcomeFragmentViewModel : ViewModel() {

    private val _isNotificationAccessPermissionEnable = MutableLiveData<Boolean>().apply {
        value = Util.notificationAccessEnable()
    }

    val isNotificationAccessPermissionEnable =
        _isNotificationAccessPermissionEnable as LiveData<Boolean>

    private val _isAllowedToNotify = MutableLiveData<Boolean>().apply {
        value = Util.notificationEnable()
    }

    val isAllowedToNotify = _isAllowedToNotify as LiveData<Boolean>

    private val _isIgnoreBatteryOptimization = MutableLiveData<Boolean>().apply {
        value = Util.ignoreBatteryOptimizations()
    }

    val isIgnoreBatteryOptimization = _isIgnoreBatteryOptimization as LiveData<Boolean>

    fun flushAccessNotificationPermission() {
        _isNotificationAccessPermissionEnable.value = Util.notificationAccessEnable()
    }

    fun flushNotificationPermission() {
        _isAllowedToNotify.value = Util.notificationEnable()
    }

    fun flushIgnoreBatteryOptimization() {
        _isIgnoreBatteryOptimization.value = Util.ignoreBatteryOptimizations()
    }

    fun flushPermission() {
        flushAccessNotificationPermission()
        flushNotificationPermission()
        flushIgnoreBatteryOptimization()
    }
}