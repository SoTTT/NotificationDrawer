package com.sottt.notificationdrawer.filter

import android.os.Parcel
import android.os.Parcelable

data class PackageFilterData(
    val tag: String?,
    val valid: Boolean,
    val name: String?,
    val packageNameList: List<String>?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.createStringArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tag)
        parcel.writeByte(if (valid) 1 else 0)
        parcel.writeString(name)
        parcel.writeStringList(packageNameList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PackageFilterData> {
        override fun createFromParcel(parcel: Parcel): PackageFilterData {
            return PackageFilterData(parcel)
        }

        override fun newArray(size: Int): Array<PackageFilterData?> {
            return arrayOfNulls(size)
        }
    }

}
