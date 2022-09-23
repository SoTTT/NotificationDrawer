package com.sottt.notificationdrawer.filter

import java.io.Serializable
import java.util.*

abstract class AbstractFilter : Checkable, Serializable, Cloneable {

    var tag: String = ""
    var valid: Boolean = true

    var name: String? = null

    val key by lazy {
        tag + name
    }
}