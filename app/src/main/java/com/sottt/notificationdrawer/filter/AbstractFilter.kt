package com.sottt.notificationdrawer.filter

import java.io.Serializable

abstract class AbstractFilter : Checkable, Serializable, Cloneable {

    var tag: String = ""
    var valid: Boolean = true
}