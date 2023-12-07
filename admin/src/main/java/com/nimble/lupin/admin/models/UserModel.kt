package com.nimble.lupin.admin.models

import android.os.Parcel
import android.os.Parcelable

data class UserModel(
    var userId: Int,
    var firstName:  String,
    var lastName:  String,
    var mobileNumber:  String,
    var emailId: String,
    var workStation:  String,
    var post: Int,
    var employeeId: String,
    var reportAuthority: Int,
    var joiningDate:  String,
    var profilePhoto :String,

    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(mobileNumber)
        parcel.writeString(emailId)
        parcel.writeString(workStation)
        parcel.writeInt(post)
        parcel.writeString(employeeId)
        parcel.writeInt(reportAuthority)
        parcel.writeString(joiningDate)
        parcel.writeString(profilePhoto)
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
