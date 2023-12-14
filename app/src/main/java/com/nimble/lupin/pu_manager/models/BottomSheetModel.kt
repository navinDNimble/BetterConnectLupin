package com.nimble.lupin.pu_manager.models



data class BottomSheetModel(
    var id  : Int,
    val title: String,
    val optionalChooser :Int = 0
)
