package com.nimble.lupin.admin.views.navigation.user.userTaskList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.adapters.TaskUsersAdapter
import com.nimble.lupin.admin.adapters.UserTasksAdapter
import com.nimble.lupin.admin.databinding.FragmentUserTaskListBinding
import com.nimble.lupin.admin.interfaces.OnUserTaskSelected
import com.nimble.lupin.admin.models.UserModel
import com.nimble.lupin.admin.models.UserTasksListModel
import com.nimble.lupin.admin.utils.Constants
import com.nimble.lupin.admin.utils.PaginationScrollListener
import com.nimble.lupin.admin.views.home.MainActivity
import com.nimble.lupin.admin.views.home.schedule.ScheduleFragmentDirections
import com.nimble.lupin.admin.views.navigation.user.UserListFragmentDirections

class UserTaskListFragment : Fragment() ,OnUserTaskSelected{

    private lateinit var viewModel: UserTaskListViewModel
    private var _binding: FragmentUserTaskListBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskListProgress: MutableList<UserTasksListModel>
    private lateinit var taskListCompleted: MutableList<UserTasksListModel>
    private lateinit var progressAdapter: UserTasksAdapter
    private lateinit var completedAdapter: UserTasksAdapter
    private lateinit var completedPaginationScrollListener: PaginationScrollListener
    private lateinit var progressPaginationScrollListener: PaginationScrollListener

    private   var  userModel  : UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userModel = arguments?.getParcelable("UserDetail")
        viewModel = ViewModelProvider(this)[UserTaskListViewModel::class.java]

         resetVariables()

        progressAdapter = UserTasksAdapter(taskListProgress, this)
        completedAdapter = UserTasksAdapter(taskListCompleted, this)
        viewModel.pendingTaskListResponse.observe(this, Observer {
            if (viewModel.pendingPage==0){
                taskListProgress.clear()
            }
            taskListProgress.addAll(it)
            Log.d("sachintask", taskListProgress.size.toString())
            progressAdapter.updateList(taskListProgress)
            progressAdapter.notifyDataSetChanged()
        })
        viewModel.completedTaskListResponse.observe(this, Observer {
            if (viewModel.completedPage==0){
                taskListCompleted.clear()
            }
            taskListCompleted.addAll(it)
            completedAdapter.updateList(taskListCompleted)
            completedAdapter.notifyDataSetChanged()
        })

        Log.d("sachintask", "Loading Task")
        viewModel.getPendingUserTask(userModel!!.userId)
        viewModel.getCompletedUserTask(userModel!!.userId)
        _binding = FragmentUserTaskListBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.textViewUserName.text = userModel?.firstName+" " + userModel?.lastName
        binding.textViewUserPost.text = when (userModel?.post) {
            1 -> "Field Facilitator"
            else -> "Unknown Post"
        }
        binding.imageViewBackArrow.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        binding.assigntaskToUser.setOnClickListener {
            val action =  UserTaskListFragmentDirections.userListFragmentToAssignTaskFragment(userModel!!)
            findNavController().navigate(action)
        }
        binding.progressTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.completedTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.progressTaskRecyclerView.adapter = progressAdapter
        binding.completedTaskRecyclerView.adapter = completedAdapter

        progressPaginationScrollListener = object :
            PaginationScrollListener(binding.progressTaskRecyclerView.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return viewModel.isLastPageOfPending
            }

            override fun isLoading(): Boolean {
                return viewModel.LoadingPendingTask.get()!!
            }

            override fun loadMoreItems() {

                if (!viewModel.isLastPageOfPending) {
                    viewModel.pendingPage += Constants.PAGE_SIZE
                    viewModel.getPendingUserTask(userModel!!.userId)
                }
            }
        }
        progressPaginationScrollListener.let { progressPaginationScrollListener ->
            binding.progressTaskRecyclerView.addOnScrollListener(
                progressPaginationScrollListener
            )
        }

        completedPaginationScrollListener = object :
            PaginationScrollListener(binding.completedTaskRecyclerView.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return viewModel.isLastPageOfCompleted
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoadingCompletedTask.get()!!
            }

            override fun loadMoreItems() {
                if (!viewModel.isLastPageOfCompleted) {
                    viewModel.completedPage += Constants.PAGE_SIZE
                    viewModel.getCompletedUserTask(userModel!!.userId)
                }

            }
        }
        completedPaginationScrollListener.let { completedPaginationScrollListener ->
            binding.completedTaskRecyclerView.addOnScrollListener(
                completedPaginationScrollListener
            )
        }

        binding.pendingTaskView.setOnClickListener {
            if (viewModel.completedRecyclerViewVisibility.get() == true) {
                viewModel.completedRecyclerViewVisibility.set(false)
            }

            viewModel.pendingRecyclerViewVisibility.set(
                viewModel.pendingRecyclerViewVisibility.get()?.not()
            )
        }
        binding.completedTaskView.setOnClickListener {
            if (viewModel.pendingRecyclerViewVisibility.get() == true) {
                viewModel.pendingRecyclerViewVisibility.set(false)
            }
            viewModel.completedRecyclerViewVisibility.set(
                viewModel.completedRecyclerViewVisibility.get()?.not()
            )
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    fun resetVariables() {
        viewModel.pendingPage =0
        viewModel.completedPage=0
        viewModel.isLastPageOfCompleted =false
        viewModel.isLastPageOfPending = false

        taskListProgress = mutableListOf()
        taskListCompleted = mutableListOf()
    }
    override fun onResume() {
        super.onResume()

        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
        if (Constants.isChanged){
            resetVariables()
            viewModel.getPendingUserTask(userModel!!.userId)
            viewModel.getCompletedUserTask(userModel!!.userId)
            Constants.isChanged =false

        }
    }

    override fun onUserTaskSelected(userTaskListViewModel: UserTasksListModel) {
        val action  = UserTaskListFragmentDirections.userTaskListFragmentToScheduleUpdatesFragment(userTaskListViewModel.userTask,userTaskListViewModel.task)
        findNavController().navigate(action)
    }

}