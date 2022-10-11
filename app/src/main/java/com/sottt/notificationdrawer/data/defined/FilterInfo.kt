package com.sottt.notificationdrawer.data.defined

data class FilterInfo(val name: String, val tag: String) {
    val key get() = tag + name
}
