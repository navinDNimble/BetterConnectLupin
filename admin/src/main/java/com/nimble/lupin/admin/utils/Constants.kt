package com.nimble.lupin.admin.utils

class Constants {
    companion object {
        const val  serverIpAddress = "10.0.2.2" // Replace with your server's IP address
        const val  serverPort = 5000 // Replace with your server's port
        const val BASE_URL = "http://$serverIpAddress:$serverPort"

        const val SHARED_PREF_KEY = "USER_KEY"
        const val Admin_ID_Key = "ADMIN_ID"
        const val Admin_Username_Key = "ADMIN_USER_NAME"
        const val adminProfileModel = "ADMIN_PROFILE_MODEL"
        var Admin_ID = 0
        const val PAGE_SIZE = 1
        var isChanged = false

    }
}