package com.nimble.lupin.user.models

import android.os.Parcel
import android.os.Parcelable

data class UserProfileModel(
    val userId: Int,
    val firstName: String?,
    val lastName: String?,
    val mobileNumber: String?,
    val emailId: String?,
    val workStation: String?,
    val post: Int,
    val employeeId: Int,
    val reportAuthority: Int,
    val joiningDate: String?,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(mobileNumber)
        parcel.writeString(emailId)
        parcel.writeString(workStation)
        parcel.writeInt(post)
        parcel.writeInt(employeeId)
        parcel.writeInt(reportAuthority)
        parcel.writeString(joiningDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserProfileModel> {
        override fun createFromParcel(parcel: Parcel): UserProfileModel {
            return UserProfileModel(parcel)
        }

        override fun newArray(size: Int): Array<UserProfileModel?> {
            return arrayOfNulls(size)
        }
    }
}
