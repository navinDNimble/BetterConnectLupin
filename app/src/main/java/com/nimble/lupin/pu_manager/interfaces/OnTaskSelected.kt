package com.nimble.lupin.pu_manager.interfaces

import com.nimble.lupin.pu_manager.models.TaskModel

interface OnTaskSelected {
    fun onTaskSelected(taskModel: TaskModel)
}