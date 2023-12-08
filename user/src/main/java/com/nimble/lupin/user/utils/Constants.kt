package com.nimble.lupin.user.utils

import com.google.firebase.storage.FirebaseStorage

class Constants {
    companion object {

      //  const val  serverIpAddress = "10.0.2.2"
//        const val  serverIpAddress = "192.168.1.5"
      //  const val  serverIpAddress = "10.0.2.2"
//        const val  serverPort = 5000
        //const val BASE_URL = "http://$serverIpAddress:$serverPort"
        const val BASE_URL = "http://192.168.1.8:5000/"
        const val SHARED_PREF_KEY = "USER_KEY"
        const val User_ID = "USER_ID"
        const val User_Name = "USER_NAME"
        const val User_IMAGE = "USER_IMAGE"
        const val User_Profile = "USER_PROFILE"
        var userId  :Int = 0
        const val PAGE_SIZE = 1
        val storageRef = FirebaseStorage.getInstance().reference
        var changedSize = 0
    }
}