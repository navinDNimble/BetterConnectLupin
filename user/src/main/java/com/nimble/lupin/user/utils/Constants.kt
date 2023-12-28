package com.nimble.lupin.user.utils

import com.google.firebase.storage.FirebaseStorage

class Constants {
    companion object {

        //        const val BASE_URL = "http://192.168.1.19:5000/"
        const val BASE_URL = "http://13.201.86.137:5000/"
        const val SHARED_PREF_KEY = "USER_KEY"
        const val User_ID = "USER_ID"
        const val User_Name = "USER_NAME"
        const val User_IMAGE = "USER_IMAGE"
        const val User_Profile = "USER_PROFILE"
        const val User_workStation = "USER_WORKSTATION"
        var userId: Int = 0
        const val PAGE_SIZE = 1
        val storageRef = FirebaseStorage.getInstance().reference
        var changedSize = 0

        val ACCESS_ID: String = "AKIA3PVIINNRR72CJHVM"
        val SECRET_KEY: String = "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"
        val BUCKET_NAME: String = "bettercotton"
    }
}