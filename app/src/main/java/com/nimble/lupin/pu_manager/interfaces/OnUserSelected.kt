package com.nimble.lupin.pu_manager.interfaces

import com.nimble.lupin.pu_manager.models.UserModel

interface OnUserSelected {
    fun onUserSelected(userModel: UserModel)
}