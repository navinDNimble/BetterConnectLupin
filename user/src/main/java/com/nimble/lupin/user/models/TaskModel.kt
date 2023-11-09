package com.nimble.lupin.user.models

import android.os.Parcel
import android.os.Parcelable

data class TaskModel(
    val id:Int,
    val taskName: String?,
    val startDate: String?,
    val endDate: String?,
    val activityId:Int,
    val subActivityId:Int,
    val taskModeId:Int,
    val userTaskId: Int,
    val activityName: String?,
    val subActivityName: String?,
    val completedUnits:Int,
    val TotalUnits:Int
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(taskName)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeInt(activityId)
        parcel.writeInt(subActivityId)
        parcel.writeInt(taskModeId)
        parcel.writeInt(userTaskId)
        parcel.writeString(activityName)
        parcel.writeString(subActivityName)
        parcel.writeInt(completedUnits)
        parcel.writeInt(TotalUnits)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskModel> {
        override fun createFromParcel(parcel: Parcel): TaskModel {
            return TaskModel(parcel)
        }

        override fun newArray(size: Int): Array<TaskModel?> {
            return arrayOfNulls(size)
        }
    }

}

