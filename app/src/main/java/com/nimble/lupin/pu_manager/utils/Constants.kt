package com.nimble.lupin.pu_manager.utils

import com.google.firebase.storage.FirebaseStorage

class Constants {
    companion object {
        //  const val  serverIpAddress = "3.80.184.59" // Replace with your server's IP address
//        private const val  serverIpAddress = "10.0.2.2" //Testing local Ip Address
//        private const val  serverPort = 5000 // Replace with your server's port
//        const val BASE_URL = "http://$serverIpAddress:$serverPort"
      const val BASE_URL = "http://192.168.1.16:5000/"
        //const val BASE_URL = "http://13.126.217.55:5000/"
        const val SHARED_PREF_KEY = "USER_KEY"
        const val Admin_ID_Key = "ADMIN_ID"
        const val Admin_Username_Key = "ADMIN_USER_NAME"
        const val Admin_Role_Key = "ADMIN_ROLE"
        const val Admin_WorkstationId_Key = "ADMIN_WORKSTATION_ID"
        const val Admin_WorkStationName_Key = "ADMIN_WORKSTATION_NAME"
        const val adminProfileModel = "ADMIN_PROFILE_MODEL"
        const val Admin_Image_Key = "ADMIN_PROFILE_IMAGE"
        var Admin_ID = 0
        var AdminWorkStation_ID = 0
        var AdminWorkStation_Name = ""
        const val PAGE_SIZE = 1
        var isChanged = false
        val storageRef = FirebaseStorage.getInstance().reference

        val ACCESS_ID:String = "AKIA3PVIINNRR72CJHVM"
        val SECRET_KEY:String = "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"
        val BUCKET_NAME: String = "bettercotton"
    }
}