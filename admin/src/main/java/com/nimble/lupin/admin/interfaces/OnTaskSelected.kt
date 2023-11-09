package com.nimble.lupin.admin.interfaces

import com.nimble.lupin.admin.models.TaskModel

interface OnTaskSelected {
    fun onTaskSelected(taskModel: TaskModel)
}