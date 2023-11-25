package com.nimble.lupin.admin.views.home.schedule.scheduleDetail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.adapters.TaskUsersAdapter
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.databinding.FragmentScheduleDetailBinding
import com.nimble.lupin.admin.interfaces.OnTaskUserSelected

import com.nimble.lupin.admin.models.TaskModel
import com.nimble.lupin.admin.models.TaskUsersModel
import com.nimble.lupin.admin.views.home.MainActivity
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScheduleDetailFragment : Fragment()  ,OnTaskUserSelected{
    private var _binding: FragmentScheduleDetailBinding? = null
    private val binding get() = _binding!!
    private   var  task  : TaskModel? = null
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    private var page =0
    private var isLastPage = false
    private lateinit var taskUsersList :MutableList<TaskUsersModel>
    private lateinit var taskUserAdapter : TaskUsersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         task = arguments?.getParcelable("TaskDetail")
        page = 0
        isLastPage = false
        taskUsersList = mutableListOf()

        taskUserAdapter = TaskUsersAdapter(taskUsersList ,this )

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentScheduleDetailBinding.inflate(inflater, container, false)

        binding.includedLayout.textViewAssignTaskTaskTitleIn.text = task?.taskName
        binding.includedLayout.textViewAssignTaskStartDateIn.text =   getString(R.string.date_combine_string, task?.startDate, task?.endDate)
        binding.includedLayout.textViewActivityNameIn.text =  getString(R.string.activity_combine_String, task?.activityName, task?.subActivityName)
        binding.includedLayout.units.visibility =View.GONE
        binding.taskDetailRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.taskDetailRecyclerView.adapter = taskUserAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTaskUsers()
    }
    private fun getTaskUsers() {
        binding.scheduleDetailProgressBar.visibility = View.VISIBLE
        apiService.getTaskUsers(task?.taskId!!,page).enqueue(object :Callback<ResponseHandler<List<TaskUsersModel>>>{
            override fun onResponse(
                call: Call<ResponseHandler<List<TaskUsersModel>>>,
                response: Response<ResponseHandler<List<TaskUsersModel>>>
            ) {
                val result = response.body()
                if (response.isSuccessful){
                    Log.d("sachin",result.toString())
                    when (result?.code) {
                        200 -> {
                            isLastPage = result.isLastPage
                            if (page==0) {
                                taskUsersList.clear()
                            }
                            taskUsersList.addAll(result.response)
                            taskUserAdapter.updateList(taskUsersList)
                            taskUserAdapter.notifyDataSetChanged()
                        }
                        404 -> {
                            isLastPage = result.isLastPage
                            binding.scheduleDetailResultTextView.text = result.message
                        }
                        409 -> {
                            isLastPage = result.isLastPage

                        }
                        500 -> {
                            showSnackBar("Error in Loading  Task")
                        }
                    }
                    binding.scheduleDetailProgressBar.visibility = View.GONE

                }
            }

            override fun onFailure(
                call: Call<ResponseHandler<List<TaskUsersModel>>>,
                t: Throwable
            ) {
                binding.scheduleDetailProgressBar.visibility = View.GONE
                showSnackBar(t.message.toString())
            }
        })
    }


    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
    }
    private fun showSnackBar(message: String) {
        val snackBar = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) };
        if (snackBar != null) {
            snackBar.setBackgroundTint(Color.RED)
            snackBar.setTextColor(Color.WHITE)
            snackBar.show()
        }
    }



    override fun onTaskUserSelected(taskUsersModel: TaskUsersModel) {
        TODO("Not yet implemented")
    }
}