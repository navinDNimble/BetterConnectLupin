package com.nimble.lupin.user.utils

import com.google.firebase.storage.FirebaseStorage

class Constants {
    companion object {

       // const val BASE_URL = "http://192.168.1.19:5000/"
        const val BASE_URL = "http://18.209.7.6:5000/"
        const val SHARED_PREF_KEY = "USER_KEY"
        const val User_ID = "USER_ID"
        const val User_Name = "USER_NAME"
        const val User_IMAGE = "USER_IMAGE"
        const val User_Profile = "USER_PROFILE"
        const val User_workStation = "USER_WORKSTATION"

        var userId  :Int = 0
        const val PAGE_SIZE = 1
        val storageRef = FirebaseStorage.getInstance().reference
        var changedSize = 0
    }
}