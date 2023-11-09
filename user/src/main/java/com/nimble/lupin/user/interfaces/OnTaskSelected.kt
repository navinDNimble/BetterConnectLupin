package com.nimble.lupin.user.interfaces

import com.nimble.lupin.user.models.TaskModel

interface OnTaskSelected {
    fun onTaskSelected(taskModel: TaskModel)
}