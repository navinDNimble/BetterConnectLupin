package com.nimble.lupin.admin.models

data class AdminProfileData(
    val userId: Int,
    val firstName: String?,
    val lastName: String?,
    val mobileNumber: String?,
    val emailId: String?,
    val workStation: Int,
    val workStationName: String,
    val post: Int,
    val employeeId: String,
    val reportAuthority: Int,
    val joiningDate: String?,
    var profilePhoto :String,
)