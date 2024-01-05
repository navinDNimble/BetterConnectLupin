package com.nimble.lupin.pu_manager.views.home.schedule.scheduleDetail

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.pu_manager.R
import com.nimble.lupin.pu_manager.adapters.TaskUsersAdapter
import com.nimble.lupin.pu_manager.api.ApiService
import com.nimble.lupin.pu_manager.api.ResponseHandler
import com.nimble.lupin.pu_manager.databinding.FragmentScheduleDetailBinding
import com.nimble.lupin.pu_manager.interfaces.OnTaskUserSelected
import com.nimble.lupin.pu_manager.models.TaskModel
import com.nimble.lupin.pu_manager.models.TaskUsersModel
import com.nimble.lupin.pu_manager.utils.Constants
import com.nimble.lupin.pu_manager.utils.PaginationScrollListener
import com.nimble.lupin.pu_manager.views.home.MainActivity
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate


class ScheduleDetailFragment : Fragment(), OnTaskUserSelected {
    private var _binding: FragmentScheduleDetailBinding? = null
    private val binding get() = _binding!!
    private var task: TaskModel? = null
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    private var page = 0
    private var isLastPage = false
    private var isLoading = false
    private lateinit var taskUsersList: MutableList<TaskUsersModel>
    private lateinit var taskUserAdapter: TaskUsersAdapter
    private lateinit var paginationScrollListener: PaginationScrollListener
    private val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = arguments?.getParcelable("TaskDetail")
        page = 0
        isLastPage = false
        taskUsersList = mutableListOf()

        taskUserAdapter = TaskUsersAdapter(taskUsersList, this)
        _binding = FragmentScheduleDetailBinding.inflate(layoutInflater)
        binding.includedLayout.textViewAssignTaskTaskTitleIn.text =
            task?.taskId.toString() + " " + task?.taskName
        binding.includedLayout.textViewAssignTaskStartDateIn.text =
            getString(R.string.date_combine_string, task?.startDate, task?.endDate)
        binding.includedLayout.textViewActivityNameIn.text =
            getString(R.string.activity_combine_String, task?.activityName, task?.subActivityName)
        binding.includedLayout.units.visibility = View.GONE

        binding.backButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        binding.assignTaskScheduleButtonId.setOnClickListener {
            val action =
                ScheduleDetailFragmentDirections.scheduleDetailFragmentToAssignTaskFragment(task!!)
            findNavController().navigate(action)
        }
        binding.scheduleUserRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.scheduleUserRecyclerView.adapter = taskUserAdapter

        paginationScrollListener = object :
            PaginationScrollListener(binding.scheduleUserRecyclerView.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if (isLastPage.not()) {
                    page += Constants.PAGE_SIZE
                    getTaskUsers()
                }
            }
        }

        paginationScrollListener.let { progressPaginationScrollListener ->
            binding.scheduleUserRecyclerView.addOnScrollListener(
                progressPaginationScrollListener
            )
        }
        getTaskUsers()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val role = sharedPref.getInt(Constants.Admin_Role_Key,0)
        if (role == 3){
            binding.assignTaskScheduleButtonId.visibility = View.GONE
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val currentDate = LocalDate.now()
//            val startDate = LocalDate.parse( task?.startDate)
//            val endDate = LocalDate.parse(task?.endDate)
//            Log.d("sachin Dates" , currentDate.toString())
//            Log.d("sachin Dates" , startDate.toString())
//            Log.d("sachin Dates" , endDate.toString())
//
//            if (currentDate in startDate..endDate) {
//                Log.d("sachin Dates", "In of range")
//            } else {
//                binding.assignTaskScheduleButtonId.visibility = View.GONE
//            }
//        }

    }

    private fun getTaskUsers() {
        binding.scheduleDetailProgressBar.visibility = View.VISIBLE
        isLoading = true
        apiService.getManagerTaskUsers(task?.taskId!!, page)
            .enqueue(object : Callback<ResponseHandler<List<TaskUsersModel>>> {
                override fun onResponse(
                    call: Call<ResponseHandler<List<TaskUsersModel>>>,
                    response: Response<ResponseHandler<List<TaskUsersModel>>>
                ) {
                    val result = response.body()
                    if (response.isSuccessful) {
                        Log.d("sachinScheduleDetails", result.toString())
                        when (result?.code) {
                            200 -> {
                                isLastPage = result.isLastPage
                                if (page == 0) {
                                    taskUsersList.clear()
                                }
                                taskUsersList.addAll(result.response)
                                taskUserAdapter.updateList(taskUsersList)
                                taskUserAdapter.notifyDataSetChanged()
                                binding.scheduleDetailResultTextView.visibility = View.GONE
                            }

                            404 -> {
                                Log.d("sachinScheduleDetails", result.toString())
                                isLastPage = result.isLastPage
                                binding.scheduleDetailResultTextView.text = result.message
                                binding.scheduleDetailResultTextView.visibility = View.VISIBLE
                            }

                            409 -> {
                                Log.d("sachinScheduleDetails", result.toString())
                                isLastPage = result.isLastPage

                            }

                            500 -> {
                                Log.d("sachinScheduleDetails", result.toString())
                                showSnackBar("Error in Loading  Task")
                            }
                        }
                        binding.scheduleDetailProgressBar.visibility = View.GONE
                        isLoading = false

                    }
                }

                override fun onFailure(
                    call: Call<ResponseHandler<List<TaskUsersModel>>>,
                    t: Throwable
                ) {
                    binding.scheduleDetailProgressBar.visibility = View.GONE
                    showSnackBar(t.message.toString())
                    isLoading = false
                }
            })
    }


    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
        if (Constants.isChanged) {
            taskUsersList.clear()
            page = 0
            taskUserAdapter.notifyDataSetChanged()
            getTaskUsers()
            Constants.isChanged = false
        }
    }

    private fun showSnackBar(message: String) {
        val rootView: View = requireActivity().findViewById(android.R.id.content)
        val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val params = snackBarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackBarView.layoutParams = params
        snackBar.setBackgroundTint(Color.RED)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()
    }


    override fun onTaskUserSelected(taskUsersModel: TaskUsersModel) {
        val action =
            ScheduleDetailFragmentDirections.scheduleDetailFragmentToScheduleUpdateFragment(
                taskUsersModel.userTask!!,
                task!!
            )
        findNavController().navigate(action)
    }
}