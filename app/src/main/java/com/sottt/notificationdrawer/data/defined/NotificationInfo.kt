package com.sottt.notificationdrawer.data.defined

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sottt.notificationdrawer.R

@Entity
data class NotificationInfo(
    var title: String,
    var content: String,
    var time: String,
    var notificationId: Int,
    var packageName: String,
    var key: String,
    @Ignore
    val smallIcon: Bitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.RGBA_F16)
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor() : this("null", "null", "null", 0, "null", "null")
}