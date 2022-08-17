package com.sottt.notificationdrawer.data.defined

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class NotificationInfo(var title: String, var content: String, var time: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}