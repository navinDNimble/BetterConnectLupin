package com.nimble.lupin.user.views.home.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nimble.lupin.user.R
import com.nimble.lupin.user.adapters.TaskAdapter
import com.nimble.lupin.user.databinding.FragmentTaskBinding
import com.nimble.lupin.user.interfaces.OnTaskSelected
import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.utils.Constants
import com.nimble.lupin.user.utils.PaginationScrollListener
import com.nimble.lupin.user.views.home.MainActivity

class TaskFragment : Fragment(), OnTaskSelected {

    private lateinit var viewModel: TaskViewModel
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private var taskListProgress: MutableList<TaskModel> = mutableListOf()
    private var taskListCompleted: MutableList<TaskModel> = mutableListOf()

    private lateinit var progressAdapter: TaskAdapter
    private lateinit var completedAdapter: TaskAdapter
    private lateinit var completedPaginationScrollListener: PaginationScrollListener
    private lateinit var progressPaginationScrollListener: PaginationScrollListener

    private var pendingPage = 0
    private var completedPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


              viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
              progressAdapter = TaskAdapter(taskListProgress, this)
              completedAdapter = TaskAdapter(taskListCompleted, this)
              viewModel.pendingTaskListResponse.observe(this, Observer {
                  taskListProgress.addAll(it)
                  progressAdapter.updateList(taskListProgress)
              })
              viewModel.completedTaskListResponse.observe(this, Observer {
                  taskListCompleted.addAll(it)
                  completedAdapter.updateList(taskListProgress)
              })
              viewModel.getPendingUserTask(pendingPage)
              viewModel.getCompletedUserTask(completedPage)
              Log.d("sachintask","oncrate")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentTaskBinding.inflate(inflater, container, false)
            binding.viewModel = viewModel
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
                    return viewModel.isLoadingPendingTask.get()!!
                }

                override fun loadMoreItems() {
                    if (viewModel.isLastPageOfPending) {
                        pendingPage += Constants.PAGE_SIZE
                        viewModel.getPendingUserTask(pendingPage)
                    }
                }
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
                    if (viewModel.isLastPageOfCompleted) {
                        completedPage += Constants.PAGE_SIZE
                        viewModel.getCompletedUserTask(completedPage)
                    }
                }
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

        return binding.root
    }



    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
    }
    override fun onTaskSelected(taskModel: TaskModel) {
        val action = TaskFragmentDirections.taskFragmentToTaskDetailFragment(taskModel)
        findNavController().navigate(action)
    }

}