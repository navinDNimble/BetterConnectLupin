package com.nimble.lupin.pu_manager.models.reportsModel

data class PdfDataModel(
    val activityId: Int,
    val activityName: String,
    val activityDescription: String,
    val totalUnits: Int,
    val totalTask: Int,
    val subActivityReportDataList: List<SubActivityReportData>,
    val headerList: List<String>,
    val imageReportList: List<String>,
    val listOf: List<String>


)
