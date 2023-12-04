package com.nimble.lupin.admin.views.home.schedule

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.adapters.TaskDetailsAdapter
import com.nimble.lupin.admin.adapters.TaskUsersAdapter
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.databinding.FragmentScheduleDetailBinding
import com.nimble.lupin.admin.databinding.FragmentScheduleUpdatesBinding
import com.nimble.lupin.admin.models.TaskModel
import com.nimble.lupin.admin.models.TaskUpdatesModel
import com.nimble.lupin.admin.models.TaskUsersModel
import com.nimble.lupin.admin.models.UserTaskModel
import com.nimble.lupin.admin.views.home.MainActivity
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScheduleUpdatesFragment : Fragment() {

    private var _binding: FragmentScheduleUpdatesBinding? = null
    private val binding get() = _binding!!
    private lateinit var tasksUpdateList :MutableList<TaskUpdatesModel>
    private lateinit var taskUserAdapter : TaskDetailsAdapter
    private   var  userTaskModel : UserTaskModel? = null
    private   var  task  : TaskModel? = null

    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userTaskModel = arguments?.getParcelable("UserTaskModel")
        task = arguments?.getParcelable("TaskDetail")
       tasksUpdateList = mutableListOf()
        taskUserAdapter = TaskDetailsAdapter(tasksUpdateList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleUpdatesBinding.inflate(inflater, container, false)

        binding.includedLayout.textViewAssignTaskTaskTitleIn.text = task?.taskName
        binding.includedLayout.textViewAssignTaskStartDateIn.text =   getString(R.string.date_combine_string, task?.startDate, task?.endDate)
        binding.includedLayout.textViewActivityNameIn.text =  getString(R.string.activity_combine_String, task?.activityName, task?.subActivityName)
        binding.includedLayout.units.visibility =View.GONE

        binding.backButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }


        binding.taskUpdatesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.taskUpdatesRecyclerView.adapter = taskUserAdapter

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTaskUpdates()
    }

    private fun getTaskUpdates() {
        binding.scheduleUpdatesProgressBar.visibility = View.VISIBLE
        apiService.getUserTaskDetails(userTaskModel!!.userTaskId)
            .enqueue(object : Callback<ResponseHandler<List<TaskUpdatesModel>>> {
                override fun onResponse(
                    call: Call<ResponseHandler<List<TaskUpdatesModel>>>,
                    response: Response<ResponseHandler<List<TaskUpdatesModel>>>
                ) {
                    val result = response.body()
                    if (result?.code!! == 200) {
                        val resultList = result.response

                        taskUserAdapter.updateList(resultList)

                    } else if (result.code == 404) {

                        binding.scheduleUpdatesResultTextView.text = result.message
                    } else if (result.code == 500){
                        showSnackBar(result.message)
                    }
                    binding.scheduleUpdatesProgressBar.visibility = View.GONE
                }

                override fun onFailure(
                    call: Call<ResponseHandler<List<TaskUpdatesModel>>>,
                    t: Throwable
                ) {
                    binding.scheduleUpdatesProgressBar.visibility = View.GONE
                    showSnackBar(t.message.toString())
                }

            }
            )
    }

    fun showSnackBar(message: String) {

        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(Color.RED)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()
    }
    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
    }

}