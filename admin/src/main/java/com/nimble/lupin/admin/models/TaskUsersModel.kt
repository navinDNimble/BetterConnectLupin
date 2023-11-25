package com.nimble.lupin.admin.models

import android.os.Parcel
import android.os.Parcelable

data class TaskUsersModel(
    val user: UserModel?,
    val userTask: UserTaskModel?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(UserModel::class.java.classLoader),
        parcel.readParcelable(UserTaskModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(user, flags)
        parcel.writeParcelable(userTask, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskUsersModel> {
        override fun createFromParcel(parcel: Parcel): TaskUsersModel {
            return TaskUsersModel(parcel)
        }

        override fun newArray(size: Int): Array<TaskUsersModel?> {
            return arrayOfNulls(size)
        }
    }
}