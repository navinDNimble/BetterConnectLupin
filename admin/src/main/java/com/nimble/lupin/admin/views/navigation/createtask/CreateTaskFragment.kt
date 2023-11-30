package com.nimble.lupin.admin.views.navigation.createtask

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.databinding.FragmentCreateTaskBinding
import com.nimble.lupin.admin.interfaces.OnBottomSheetItemSelected
import com.nimble.lupin.admin.models.BottomSheetModel
import com.nimble.lupin.admin.models.SubActivityModel
import com.nimble.lupin.admin.models.TaskCreateResponseModel
import com.nimble.lupin.admin.models.TaskModel
import com.nimble.lupin.admin.utils.BottomSheet
import com.nimble.lupin.admin.views.home.MainActivity
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CreateTaskFragment : Fragment(), OnBottomSheetItemSelected {

    private lateinit var binding: FragmentCreateTaskBinding
    private lateinit var activityBottomSheet: BottomSheet
    private lateinit var subActivityBottomSheet: BottomSheet
    private lateinit var taskModeBottomSheet: BottomSheet
    private var activityId = 0
    private var subActivityId = 0
    private var taskModeId = 0
    private val activityList = mutableListOf<BottomSheetModel>()
    private val subActivityList = mutableListOf<SubActivityModel>()
    private val subActivityBottomModelList = mutableListOf<BottomSheetModel>()
    private val taskModeList = mutableListOf<BottomSheetModel>()
    private lateinit var startDatePickerDialog: DatePickerDialog
    private lateinit var endDatePickerDialog: DatePickerDialog
    private val calendar = Calendar.getInstance()

    private lateinit var dialog: ProgressDialog
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        dialog = ProgressDialog(context)
        dialog.setMessage("Loading Activity and SubActivity") // Set the message for the dialog
        dialog.setCanceledOnTouchOutside(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        activityBottomSheet = BottomSheet(activityList, this, "activity", false, requireContext())
        subActivityBottomSheet =
            BottomSheet(subActivityBottomModelList, this, "subActivity", false, requireContext())
        taskModeBottomSheet = BottomSheet(taskModeList, this, "taskMode", false, requireContext())

        binding.activityNameTextView.setOnClickListener {
            activityBottomSheet.show()
        }

        binding.subActivityNameTextView.setOnClickListener {
            subActivityBottomSheet.show()
        }
        binding.taskModeTextView.setOnClickListener {
            taskModeBottomSheet.show()
        }
        binding.backButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        getActivitySubActivityTaskMode()




        startDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                binding.textViewStartDate.text
                calendar.set(year, monthOfYear, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate: String = sdf.format(calendar.time)
                binding.textViewStartDate.text = selectedDate
                binding.textViewEndDate.text = null
                startDatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            },

            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        )
        startDatePickerDialog.datePicker.minDate = System.currentTimeMillis()
        endDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                binding.textViewEndDate.text
                calendar.set(year, monthOfYear, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate: String = sdf.format(calendar.time)
                binding.textViewEndDate.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        binding.textViewStartDate.setOnClickListener {
            startDatePickerDialog.show()
        }
        binding.textViewEndDate.setOnClickListener {
            if (binding.textViewStartDate.text.isNullOrEmpty()) {
                showSnackBar("Please Select Start Date", Color.RED)
                return@setOnClickListener
            }
            endDatePickerDialog.datePicker.minDate = calendar.timeInMillis
            endDatePickerDialog.show()
        }


        binding.createTaskButton.setOnClickListener {
            createTask()
        }


    }

    private fun createTask() {


        if (binding.taskNameEditText.text.isNullOrEmpty()) {

            binding.taskNameEditText.requestFocus()
            binding.taskNameEditText.error = "Task Name Is Empty"
            return
        }
        if (activityId == 0) {
            binding.activityNameTextView.requestFocus()
            binding.activityNameTextView.error = " Activity Not Selected"
            return
        }
        if (subActivityId == 0 && subActivityBottomModelList.size != 0) {
            binding.subActivityNameTextView.requestFocus()
            binding.subActivityNameTextView.error = "SubActivity Not Selected"
            return

        }
        if (taskModeId == 0) {
            binding.taskModeTextView.error = "Mode Of Training  Not Selected"
            binding.taskModeTextView.requestFocus()
            return
        }
        if (binding.textViewStartDate.text.isNullOrEmpty()) {
            binding.textViewStartDate.error = "Start Date  Not Selected"
            binding.textViewStartDate.requestFocus()
            return
        }
        if (binding.textViewEndDate.text.isNullOrEmpty()) {
            binding.textViewEndDate.error = "End Date  Not Selected"
            binding.textViewEndDate.requestFocus()
            return
        }
        val taskName = binding.taskNameEditText.text.toString()
        val activityName = binding.activityNameTextView.text.toString()
        val subActivityName = binding.subActivityNameTextView.text.toString()
        val taskModeName = binding.taskModeTextView.text.toString()
        val startDate = binding.textViewStartDate.text.toString()
        val endDate = binding.textViewEndDate.text.toString()

        val task = TaskModel(
            0,
            taskName,
            activityId,
            subActivityId,
            activityName,
            subActivityName,
            taskModeName,
            taskModeId,
            startDate,
            endDate,
            0,
            -1,
            1
        )
        binding.createTaskProgressBar.visibility = View.VISIBLE
        binding.createTaskButton.visibility = View.GONE
        apiService.createTask(task).enqueue(object : Callback<ResponseHandler<TaskModel>> {
            override fun onResponse(
                call: Call<ResponseHandler<TaskModel>>,
                response: Response<ResponseHandler<TaskModel>>
            ) {
                val result = response.body()
                if (result?.code == 200) {
                    showSnackBar(result.message, Color.GREEN)
                    val action = CreateTaskFragmentDirections.createTaskFragmentToScheduleFragment()
                    findNavController().navigate(action)
                } else {
                    showSnackBar(result?.message.toString(), Color.RED)
                    binding.createTaskButton.visibility = View.VISIBLE
                    binding.createTaskProgressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseHandler<TaskModel>>, t: Throwable) {
                showSnackBar(t.message.toString(), Color.RED)
                binding.createTaskButton.visibility = View.VISIBLE
                binding.createTaskProgressBar.visibility = View.GONE
            }

        })
    }

    private fun getActivitySubActivityTaskMode() {
        dialog.show()
        apiService.getActivitySubActivityTaskMode()
            .enqueue(object : Callback<ResponseHandler<TaskCreateResponseModel>> {
                override fun onResponse(
                    call: Call<ResponseHandler<TaskCreateResponseModel>>,
                    response: Response<ResponseHandler<TaskCreateResponseModel>>
                ) {
                    val result = response.body()
                    if (result?.code == 200) {
                        result.response.activities.forEach {
                            activityList.add(BottomSheetModel(it.activityId, it.activityName))
                        }
                        subActivityList.addAll(result.response.subActivities)
                        result.response.taskModes.forEach {
                            taskModeList.add(BottomSheetModel(it.taskModeId, it.taskModeName))
                        }

                        activityBottomSheet.updateList(activityList)
                        taskModeBottomSheet.updateList(taskModeList)


                    } else {
                        showSnackBar(result?.message.toString(), Color.RED)

                    }
                    dialog.cancel()
                }

                override fun onFailure(
                    call: Call<ResponseHandler<TaskCreateResponseModel>>,
                    t: Throwable
                ) {
                    showSnackBar(t.message.toString(), Color.RED)
                    dialog.cancel()
                }

            })


    }

    private fun filterSubActivityList(activityId: Int) {
        subActivityBottomModelList.clear()

        subActivityList.forEach {
            if (it.activityId == activityId) {
                subActivityBottomModelList.add(
                    BottomSheetModel(
                        it.subActivityId,
                        it.subActivityName
                    )
                )
            }
        }
        subActivityBottomSheet.updateList(subActivityBottomModelList)
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
    }


    override fun onBottomSheetItemSelected(bottomSheetItem: BottomSheetModel, type: String) {
        when (type) {

            "activity" -> {
                binding.activityNameTextView.text = bottomSheetItem.title
                activityId = bottomSheetItem.id
                filterSubActivityList(activityId)
                binding.subActivityNameTextView.text = null
                subActivityId = 0
                activityBottomSheet.cancel()
            }

            "subActivity" -> {
                binding.subActivityNameTextView.text = bottomSheetItem.title
                subActivityId = bottomSheetItem.id
                subActivityBottomSheet.cancel()
            }

            "taskMode" -> {
                binding.taskModeTextView.text = bottomSheetItem.title
                taskModeId = bottomSheetItem.id
                taskModeBottomSheet.cancel()
            }
        }
    }

    private fun showSnackBar(message: String, color: Int) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(color)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()

    }

}