package com.nimble.lupin.admin.interfaces

import com.nimble.lupin.admin.models.UserModel

interface OnUserSelected {
    fun onUserSelected(userModel: UserModel)
}