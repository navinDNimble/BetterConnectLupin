package com.nimble.lupin.admin.models

data class AssignTaskModel(
    var userId: Int,
    var firstName:  String,
    var lastName:  String,
    var workStation:  Int,
    var total_units : String? =null,
    var isSelected : Boolean =false,
)
