package com.nimble.lupin.admin.utils

import com.google.firebase.storage.FirebaseStorage

class Constants {
    companion object {
        const val  serverIpAddress = "3.80.184.59" // Replace with your server's IP address
      //  const val  serverIpAddress = "10.0.2.2" //Testing local Ip Address
        const val  serverPort = 5000 // Replace with your server's port
        const val BASE_URL = "http://$serverIpAddress:$serverPort"
        const val SHARED_PREF_KEY = "USER_KEY"
        const val Admin_ID_Key = "ADMIN_ID"
        const val Admin_Username_Key = "ADMIN_USER_NAME"
        const val adminProfileModel = "ADMIN_PROFILE_MODEL"
        const val Admin_Image_Key = "ADMIN_PROFILE_IMAGE"

        var Admin_ID = 0
        const val PAGE_SIZE = 1
        var isChanged = false
        val storageRef = FirebaseStorage.getInstance().reference

    }
}