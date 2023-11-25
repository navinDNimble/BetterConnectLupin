package com.nimble.lupin.admin.models

import android.os.Parcel
import android.os.Parcelable

data class UserModel(

    var emailId: String?,
    var employeeId: String?,
    var firstName:  String?,
    var joiningDate:  String?,
    var lastName:  String?,
    var mobileNumber:  String?,
    var post: Int,
    var reportAuthority: Int,
    var userId: Int,
    var workStation:  String?,

    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
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
        parcel.writeString(emailId)
        parcel.writeString(employeeId)
        parcel.writeString(firstName)
        parcel.writeString(joiningDate)
        parcel.writeString(lastName)
        parcel.writeString(mobileNumber)
        parcel.writeInt(post)
        parcel.writeInt(reportAuthority)
        parcel.writeInt(userId)
        parcel.writeString(workStation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}
