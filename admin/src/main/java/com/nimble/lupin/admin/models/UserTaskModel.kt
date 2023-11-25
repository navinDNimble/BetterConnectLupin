package com.nimble.lupin.admin.models

data class UserTaskModel(
    val completedUnit: Int,
    val isTaskComplete: Int,
    val taskId: Int,
    val totalUnits: Int,
    val userId: Int,
    val userTaskId: Int,
)
