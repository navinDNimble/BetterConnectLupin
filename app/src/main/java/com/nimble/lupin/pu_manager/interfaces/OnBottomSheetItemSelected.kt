package com.nimble.lupin.pu_manager.interfaces

import com.nimble.lupin.pu_manager.models.BottomSheetModel

interface OnBottomSheetItemSelected {
    fun onBottomSheetItemSelected(bottomSheetItem: BottomSheetModel, type : String)


}