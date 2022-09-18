package com.sottt.notificationdrawer.data.defined

data class FilterInfo(val name: String, val tag: String) {
    val id get() = "${tag}_${name}_"
}
