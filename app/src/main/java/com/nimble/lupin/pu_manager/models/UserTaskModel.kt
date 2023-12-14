package com.nimble.lupin.pu_manager.models

import android.os.Parcel
import android.os.Parcelable

data class UserTaskModel(
    val completedUnit: Int,
    val isTaskComplete: Int,
    val taskId: Int,
    val totalUnits: Int,
    val userId: Int,
    val userTaskId: Int,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(completedUnit)
        parcel.writeInt(isTaskComplete)
        parcel.writeInt(taskId)
        parcel.writeInt(totalUnits)
        parcel.writeInt(userId)
        parcel.writeInt(userTaskId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserTaskModel> {
        override fun createFromParcel(parcel: Parcel): UserTaskModel {
            return UserTaskModel(parcel)
        }

        override fun newArray(size: Int): Array<UserTaskModel?> {
            return arrayOfNulls(size)
        }
    }
}
