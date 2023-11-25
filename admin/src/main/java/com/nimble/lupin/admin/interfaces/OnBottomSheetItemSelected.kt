package com.nimble.lupin.admin.interfaces

import com.nimble.lupin.admin.models.BottomSheetModel

interface OnBottomSheetItemSelected {
    fun onBottomSheetItemSelected(bottomSheetItem: BottomSheetModel, type : String)
}