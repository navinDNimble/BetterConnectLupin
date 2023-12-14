package com.nimble.lupin.pu_manager.interfaces

import com.nimble.lupin.pu_manager.models.UserTasksListModel

interface OnUserTaskSelected {
    fun onUserTaskSelected(userTaskListViewModel : UserTasksListModel)
}