package com.nimble.lupin.admin.views.navigation.createtask

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.nimble.lupin.admin.utils.Constants
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
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()



    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    var viewModel  :CreateTaskViewModel? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          viewModel = ViewModelProvider(this)[CreateTaskViewModel::class.java]
        viewModel!!.responseError.observe(this){
            showSnackBar(it,Color.RED)
        }


        viewModel!!.activityResponse.observe(this){ activityResponse ->
            activityResponse.forEach {
                activityList.add(BottomSheetModel(it.activityId, it.activityName))
            }
            activityBottomSheet.updateList(activityList)
        }

        viewModel!!.subActivityResponse.observe(this){
            subActivityList.addAll(it)
        }

        viewModel!!.taskModeResponse.observe(this){ taskModeResponse ->
            taskModeResponse.forEach {
                taskModeList.add(BottomSheetModel(it.taskModeId, it.taskModeName))
            }
            taskModeBottomSheet.updateList(taskModeList)
        }





    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
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

        startDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                startCalendar.set(year, monthOfYear, dayOfMonth)
                binding.textViewStartDate.text
                startCalendar.set(year, monthOfYear, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate: String = sdf.format(startCalendar.time)
                binding.textViewStartDate.text = selectedDate
                binding.textViewEndDate.text = null

            },

            startCalendar.get(Calendar.YEAR),
            startCalendar.get(Calendar.MONTH),
            startCalendar.get(Calendar.DAY_OF_MONTH)

        )

        endDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                endCalendar.set(year, monthOfYear, dayOfMonth)
                binding.textViewEndDate.text
                endCalendar.set(year, monthOfYear, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate: String = sdf.format(endCalendar.time)
                binding.textViewEndDate.text = selectedDate
            },
            endCalendar.get(Calendar.YEAR),
            endCalendar.get(Calendar.MONTH),
            endCalendar.get(Calendar.DAY_OF_MONTH)
        )
        binding.textViewStartDate.setOnClickListener {
            startDatePickerDialog.show()
        }
        binding.textViewEndDate.setOnClickListener {
            if (binding.textViewStartDate.text.isNullOrEmpty()) {
                showSnackBar("Please Select Start Date", Color.RED)
                return@setOnClickListener
            }
            endDatePickerDialog.datePicker.minDate = startCalendar.timeInMillis
            endDatePickerDialog.show()
        }


        binding.createTaskButton.setOnClickListener {
            createTask()
        }


        viewModel?.getActivitySubActivityTaskMode()

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
            Constants.AdminWorkStation_ID,null,
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
                    Log.d("sachinCreateTask", result?.response.toString())
                    showSnackBar(result.message, Color.GREEN)
                    val action = CreateTaskFragmentDirections.createTaskFragmentToScheduleFragment()
                    findNavController().navigate(action)
                } else {
                    Log.d("sachinCreateTask", result?.response.toString())
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



    private fun filterSubActivityList(activityId: Int) {
        subActivityBottomModelList.clear()

        subActivityList.forEach {
            if (it.activityId == activityId) {
                Log.d("sachinCreateTask", activityId.toString())
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
                Log.d("sachinCreateTask", activityId.toString())
                binding.activityNameTextView.text = bottomSheetItem.title
                activityId = bottomSheetItem.id
                filterSubActivityList(activityId)
                binding.subActivityNameTextView.text = null
                subActivityId = 0
                activityBottomSheet.cancel()
            }

            "subActivity" -> {
                Log.d("sachinCreateTask", subActivityId.toString())
                binding.subActivityNameTextView.text = bottomSheetItem.title
                subActivityId = bottomSheetItem.id
                subActivityBottomSheet.cancel()
            }

            "taskMode" -> {
                Log.d("sachinCreateTask", taskModeId.toString())
                binding.taskModeTextView.text = bottomSheetItem.title
                taskModeId = bottomSheetItem.id
                taskModeBottomSheet.cancel()
            }
        }
    }

    private fun showSnackBar(message: String, color: Int) {
        val rootView: View = requireActivity().findViewById(android.R.id.content)
        val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val params = snackBarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackBarView.layoutParams = params
        snackBar.setBackgroundTint(color)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()

    }

}