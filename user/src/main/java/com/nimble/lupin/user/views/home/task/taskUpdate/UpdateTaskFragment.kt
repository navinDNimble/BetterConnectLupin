package com.nimble.lupin.user.views.home.task.taskUpdate

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.user.R
import com.nimble.lupin.user.api.ApiService
import com.nimble.lupin.user.api.ResponseHandler
import com.nimble.lupin.user.databinding.FragmentUpdateTaskBinding
import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.models.TaskUpdatesModel
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateTaskFragment : Fragment() {

    private var _binding: FragmentUpdateTaskBinding? = null
    private val binding get() = _binding!!
    private var task: TaskModel? = null

    private lateinit  var  viewModel : UpdateTaskViewModel
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UpdateTaskViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateTaskBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        task = arguments?.getParcelable("TaskDetail")
        binding.taskNameTaskUpdate.text =  task?.task!!.taskName
        binding.dateTaskUpdate.text =  getString(R.string.date_combine_string,task?.task!!.startDate , task?.task!!.endDate)
        binding.activityTaskUpdate.text = getString(R.string.activity_combine_String,task?.task!!.activityName , task?.task!!.subActivityName)

        binding.imageViewBackButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }
      return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        visibility()

        binding.updateDetailsButton.setOnClickListener {
            val taskUpdateModel  = TaskUpdatesModel()

            if (viewModel.spinnerVisibility.get() == true)
            {
                    taskUpdateModel.spinnerSelection = binding.spinnerId.selectedItem.toString()
            }

            if (viewModel.maleCountVisibility.get() == true){

                val maleCount  = binding.maleFarmersId.text
                if (maleCount.isNullOrEmpty()){
                    binding.maleFarmersId.error = "Enter a male Farmer count"
                    binding.maleFarmersId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.male_count = maleCount.toString().toInt()

            }
            if (viewModel.femaleCountVisibility.get() == true){
                val femaleCount  = binding.femaleFarmersId.text
                if (femaleCount.isNullOrEmpty()){
                    binding.femaleFarmersId.error = "Enter a Female Farmer count"
                    binding.femaleFarmersId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.female_count = femaleCount.toString().toInt()
            }
            if (viewModel.demoCountVisibility.get() == true){
                val demoId  = binding.demoId.text
                if (demoId.isNullOrEmpty()){
                    binding.demoId.error = "Enter a Demo Count "
                    binding.demoId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.demo_count = demoId.toString().toInt()
            }
            if (viewModel.eventCountVisibility.get() == true){
                val eventCount  = binding.eventId.text
                if (eventCount.isNullOrEmpty()){
                    binding.eventId.error = "Enter a Event Count"
                    binding.eventId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.event_count = eventCount.toString().toInt()
            }
            if (viewModel.lgCodeVisibility.get() == true){
                val lgCode  = binding.LgCode.text
                if (lgCode.isNullOrEmpty()){
                    binding.LgCode.error = "Enter a Lg Code "
                    binding.LgCode.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.lg_code = lgCode.toString().toInt()
            }
            if (viewModel.wellVisibility.get() == true){
                val wellCount  = binding.wellsId.text
                if (wellCount.isNullOrEmpty()){
                    binding.wellsId.error = "Enter a Event Count"
                    binding.wellsId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.wells_count = wellCount.toString().toInt()
            }
            if (viewModel.surveyVisibility.get() == true){
                val surveyCount  = binding.surveyId.text
                if (surveyCount.isNullOrEmpty()){
                    binding.surveyId.error = "Enter a Event Count"
                    binding.surveyId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.survey_count = surveyCount.toString().toInt()
            }
            if (viewModel.villagesVisibility.get() == true){
                val villageCount  = binding.villageId.text
                if (villageCount.isNullOrEmpty()){
                    binding.villageId.error = "Enter a Event Count"
                    binding.villageId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.village_count = villageCount.toString().toInt()
            }
            if (viewModel.trainingVisibility.get() == true){
                val trainingCount  = binding.trainingId.text
                if (trainingCount.isNullOrEmpty()){
                    binding.trainingId.error = "Enter a Event Count"
                    binding.trainingId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.training_count = trainingCount.toString().toInt()
            }
            if (viewModel.finding.get() == true){
                val findings  = binding.editTextFindingsId.text
                if (findings.isNullOrEmpty()){
                    binding.editTextSubjectId.error = "Enter a Event Count"
                    binding.editTextSubjectId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.findings = findings.toString()
            }
            if (viewModel.subjectVisibility.get() == true){
                val subject  = binding.editTextSubjectId.text
                if (subject.isNullOrEmpty()){
                    binding.editTextSubjectId.error = "Enter a Event Count"
                    binding.editTextSubjectId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.subject = subject.toString()
            }
            if (viewModel.reasonForVisitVisibility.get() == true){
                val reasonForVisit  = binding.editTextReasonVisitId.text
                if (reasonForVisit.isNullOrEmpty()){
                    binding.editTextReasonVisitId.error = "Enter a Event Count"
                    binding.editTextReasonVisitId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.reasonForVisit = reasonForVisit.toString()
            }
            if (viewModel.farmerNameVisibility.get() == true){
                val farmerName  = binding.nameOfFarmer.text
                if (farmerName.isNullOrEmpty()){
                    binding.nameOfFarmer.error = "Enter a Event Count"
                    binding.nameOfFarmer.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.name_of_farmer = farmerName.toString()
            }
            if (viewModel.reasonVisibility.get() == true){
                val reason  = binding.editTextReasonId.text
                if (reason.isNullOrEmpty()){
                    binding.editTextReasonId.error = "Enter a Event Count"
                    binding.editTextReasonId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.reason = reason.toString()
            }
            if (viewModel.meetingWithWhomVisibility.get() == true){
                val meetingWithWhom  = binding.editTextMeetingWhomId.text
                if (meetingWithWhom.isNullOrEmpty()){
                    binding.editTextMeetingWhomId.error = "Enter a Event Count"
                    binding.editTextMeetingWhomId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.meeting_with_whome = meetingWithWhom.toString()
            }
            if (viewModel.photoVisibility.get() == true){

            }

            if (viewModel.finding.get() == true){
                val findings  = binding.editTextFindingsId.text
                if (findings.isNullOrEmpty()){
                    binding.editTextFindingsId.error = "Enter a Event Count"
                    binding.editTextFindingsId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.findings = findings.toString()
            }

            updateTask(taskUpdateModel)
        }

    }
    private fun updateTask(taskUpdateModel: TaskUpdatesModel) {
        viewModel.progressBarVisibility.set(true)
        apiService.updateUserTaskDetails(taskUpdateModel).enqueue(object :
            Callback<ResponseHandler<TaskUpdatesModel>>{
            override fun onResponse(
                call: Call<ResponseHandler<TaskUpdatesModel>>,
                response: Response<ResponseHandler<TaskUpdatesModel>>
            ) {
                val result = response.body()
                if (result?.code ==200){
                    showSnackBar(result.message , Color.GREEN)
                    //Todo  :go to the task details Fragment clearing the backstack
                }else if (
                    result?.code == 404
                ){
                    showSnackBar(result.message , Color.RED)
                }
                viewModel.progressBarVisibility.set(false)
            }

            override fun onFailure(call: Call<ResponseHandler<TaskUpdatesModel>>, t: Throwable) {
                showSnackBar(t.message.toString() , Color.RED)
                viewModel.progressBarVisibility.set(false)
            }

        })
    }
    fun showSnackBar(message: String , color :Int) {

        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG);
        snackBar.setBackgroundTint(color)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()
    }
    private fun visibility() {
        val subActivityId = task?.task!!.subActivityId
        when (task?.task!!.activityId) {
            1 -> {

                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)
                Log.d("sachin",task?.task!!.modeId.toString())

                when (task?.task!!.modeId) {
                    1 -> {

                        viewModel.trainingVisibility.set(true)
                    }

                    2, 3 -> {

                    }

                    4 -> {
                        viewModel.spinnerVisibility.set(true)
                    }
                }
            }

            2, 3, 4 -> {
                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)
                viewModel.trainingVisibility.set(true)

            }

            5, 6 -> {
                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)
            }

            7 -> {
                viewModel.reasonForVisitVisibility.set(true)
                viewModel.finding.set(true)

            }

            8 -> {
                when (subActivityId) {
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 -> {

                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                        viewModel.demoCountVisibility.set(true)

                    }


                }
            }

            9 -> {
                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)
                viewModel.eventCountVisibility.set(true)
            }

            10 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                        viewModel.lgCodeVisibility.set(true)
                    }

                    4 -> {
                        viewModel.wellVisibility.set(true)

                    }

                    5 -> {
                        viewModel.subjectVisibility.set(true)
                        viewModel.farmerNameVisibility.set(true)

                    }
                }

            }

            11 -> {
                when (subActivityId) {
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 -> {
                        viewModel.surveyVisibility.set(true)
                    }
                }
            }

            12 -> {
                when (subActivityId) {
                    1 -> {
                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                   }
                }
            }

            13 -> {
                when (subActivityId) {
                    1, 2, 3, 4, 5 -> {
                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                    }
                }
            }

            14 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                    }
                }
            }

            15 -> {
                when (subActivityId) {
                    1, 2, 3, 4, 5 -> {
                        viewModel.reasonVisibility.set(true)
                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                    }

                    6, 7 -> {
                        viewModel.meetingWithWhomVisibility .set(true)

                    }
                }
            }

            16 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)

                    }
                }
            }

            17 -> {
                when (subActivityId) {
                    1, 2, 3 -> {

                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                        viewModel.villagesVisibility .set(true)
                    }
                }

            }

            18 -> {
                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)
            }

            19 -> {
                when (subActivityId) {
                    1, 2, 3, 4 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            20 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            21 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            22 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            23 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            24 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            25 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            26 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            27 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            28 -> {
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }

            29 -> {
                when (subActivityId) {
                    1, 2 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                    }
                }
            }
        }
    }
}