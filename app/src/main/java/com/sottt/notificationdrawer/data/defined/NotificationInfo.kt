package com.sottt.notificationdrawer.data.defined

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class NotificationInfo(
    var title: String,
    var content: String,
    var time: String,
    var notificationId: Int
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}