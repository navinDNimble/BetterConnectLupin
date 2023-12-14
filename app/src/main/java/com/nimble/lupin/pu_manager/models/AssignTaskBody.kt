package com.nimble.lupin.pu_manager.models

data class AssignTaskBody (val taskId :Int , val userList : MutableSet<AssignTaskModel>)