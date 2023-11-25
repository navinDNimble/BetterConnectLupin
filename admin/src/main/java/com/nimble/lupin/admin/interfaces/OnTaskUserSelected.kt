package com.nimble.lupin.admin.interfaces

import com.nimble.lupin.admin.models.TaskUsersModel

interface OnTaskUserSelected {
    fun onTaskUserSelected(taskUsersModel: TaskUsersModel)
}