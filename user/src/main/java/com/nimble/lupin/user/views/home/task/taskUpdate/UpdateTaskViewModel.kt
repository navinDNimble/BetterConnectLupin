package com.nimble.lupin.user.views.home.task.taskUpdate

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class UpdateTaskViewModel : ViewModel() {
    var progressBarVisibility = ObservableField(false)

    var maleCountVisibility = ObservableField(false)
    var femaleCountVisibility = ObservableField(false)

    var lgCodeVisibility = ObservableField(false)
    var wellVisibility = ObservableField(false)
    var surveyVisibility = ObservableField(false)
    var no_of_farmers = ObservableField(false)
    var villagesVisibility = ObservableField(false)

    var subjectVisibility = ObservableField(false)
    var reasonForVisitVisibility = ObservableField(false)
    var farmerNameVisibility = ObservableField(false)
    var reasonVisibility = ObservableField(false)
    var meetingWithWhomVisibility = ObservableField(false)
    var photoVisibility = ObservableField(false)
    var photoMessage= ObservableField("")
    var finding = ObservableField(false)
}