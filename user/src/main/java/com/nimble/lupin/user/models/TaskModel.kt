package com.nimble.lupin.user.models

import android.os.Parcel
import android.os.Parcelable

data class TaskModel(
    val task:Task?,
    val userTask: UserTaskModel?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Task::class.java.classLoader),
        parcel.readParcelable(UserTaskModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(task, flags)
        parcel.writeParcelable(userTask, flags)
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

data class Task(
    val taskId:Int,
    val taskName: String?,
    val startDate: String?,
    val endDate: String?,
    val activityId:Int,
    val subActivityId:Int,
    val modeId:Int,
    val userTaskId: Int,
    val activityName: String?,
    val subActivityName: String?,

    ):Parcelable {
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(taskId)
        parcel.writeString(taskName)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeInt(activityId)
        parcel.writeInt(subActivityId)
        parcel.writeInt(modeId)
        parcel.writeInt(userTaskId)
        parcel.writeString(activityName)
        parcel.writeString(subActivityName)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}

data class UserTaskModel(
    var completedUnit: Int,
    val isTaskComplete: Int,
    val taskId: Int,
    val totalUnits: Int,
    val userId: Int,
    val userTaskId: Int
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

