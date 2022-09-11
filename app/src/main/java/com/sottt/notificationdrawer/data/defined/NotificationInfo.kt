package com.sottt.notificationdrawer.data.defined

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sottt.notificationdrawer.R
import java.io.Serializable

@Entity
data class NotificationInfo(
    var title: String,
    var content: String,
    var time: String,
    var notificationId: Int,
    var packageName: String,
    var key: String,
    @Ignore
    var smallIcon: Bitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.RGBA_F16)
) : Serializable, Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Bitmap::class.java.classLoader)!!
    ) {
        id = parcel.readLong()
    }

    constructor() : this("null", "null", "null", 0, "null", "null")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(time)
        parcel.writeInt(notificationId)
        parcel.writeString(packageName)
        parcel.writeString(key)
        parcel.writeParcelable(smallIcon, flags)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationInfo> {
        override fun createFromParcel(parcel: Parcel): NotificationInfo {
            return NotificationInfo(parcel)
        }

        override fun newArray(size: Int): Array<NotificationInfo?> {
            return arrayOfNulls(size)
        }
    }
}