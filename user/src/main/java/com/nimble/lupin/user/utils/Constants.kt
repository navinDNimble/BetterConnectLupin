package com.nimble.lupin.user.utils

class Constants {
    companion object {

        const val  serverIpAddress = "10.0.2.2" // Replace with your server's IP address

        const val  serverPort = 5000 // Replace with your server's port



        const val BASE_URL = "http://$serverIpAddress:$serverPort"
// Make your network request using serverUrl

        const val SHARED_PREF_KEY = "USER_KEY"
        const val User_ID = "USER_ID"
        const val User_Name = "USER_NAME"
        const val User_Profile = "USER_PROFILE"
        var userId  :Int = 0
        const val PAGE_SIZE = 1
    }
}