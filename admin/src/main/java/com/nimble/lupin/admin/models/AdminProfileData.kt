package com.nimble.lupin.admin.models

data class AdminProfileData(
    val userId: Int,
    val firstName: String?,
    val lastName: String?,
    val mobileNumber: String?,
    val emailId: String?,
    val workStation: String?,
    val post: Int,
    val employeeId: Int,
    val reportAuthority: Int,
    val joiningDate: String?,
    var profilePhoto :String,
)