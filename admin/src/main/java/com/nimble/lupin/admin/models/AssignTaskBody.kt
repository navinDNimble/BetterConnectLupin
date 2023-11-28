package com.nimble.lupin.admin.models

data class AssignTaskBody (val taskId :Int , val userList : MutableSet<AssignTaskModel>)