package com.nimble.lupin.admin.models

import android.os.Parcel
import android.os.Parcelable


data class TaskModel(
    val taskId:Int,
    val taskName: String?,
    val activityId:Int,
    val subActivityId:Int,
    val activityName: String?,
    val subActivityName: String?,
    val modeName: String?,
    val modeId: Int,
    val startDate: String?,
    val endDate: String?,
    val user_alloted: Int?,
    val user_completed_task: Int?,
    val createdBy: Int?, ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(taskId)
        parcel.writeString(taskName)
        parcel.writeInt(activityId)
        parcel.writeInt(subActivityId)
        parcel.writeString(activityName)
        parcel.writeString(subActivityName)
        parcel.writeString(modeName)
        parcel.writeInt(modeId)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeValue(user_alloted)
        parcel.writeValue(user_completed_task)
        parcel.writeValue(createdBy)
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