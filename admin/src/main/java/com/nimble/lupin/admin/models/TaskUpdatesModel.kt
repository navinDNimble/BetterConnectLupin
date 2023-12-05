package com.nimble.lupin.admin.models


data class TaskUpdatesModel(
    var taskUpdateId: Int? = null,
    var userId: Int? = null,
    var taskId: Int? = null,
    var userTaskId: Int? =null,

    var male_count: Int? =null,
    var female_count: Int?= null,


    var lg_code: Int? = null,
    var wells_count: Int? = null,
    var survey_count: Int? = null,
    var village_count: Int? = null,

    var no_of_farmers: Int? = null,

    var findings: String? =null,
    var subject: String? =null,
    var reasonForVisit: String? = null,
    var reason: String? = null,
    var meeting_with_whome: String? = null,
    var name_of_farmer: String? = null,


    var photo: Int? = null,
    var update_date : String? =  null
)