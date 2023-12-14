package com.nimble.lupin.pu_manager.interfaces

import com.nimble.lupin.pu_manager.models.TaskUsersModel

interface OnTaskUserSelected {
    fun onTaskUserSelected(taskUsersModel: TaskUsersModel)
}