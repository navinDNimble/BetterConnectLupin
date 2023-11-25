package com.nimble.lupin.admin.interfaces

import com.nimble.lupin.admin.models.UserTaskModel
import com.nimble.lupin.admin.models.UserTasksListModel
import com.nimble.lupin.admin.views.navigation.user.userTaskList.UserTaskListViewModel

interface OnUserTaskSelected {
    fun onUserTaskSelected(userTaskListViewModel : UserTasksListModel)
}