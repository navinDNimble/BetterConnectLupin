package com.nimble.lupin.user.views.home.task

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.user.adapters.TaskAdapter
import com.nimble.lupin.user.databinding.FragmentTaskBinding
import com.nimble.lupin.user.interfaces.OnTaskSelected
import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.utils.Constants
import com.nimble.lupin.user.utils.PaginationScrollListener
import com.nimble.lupin.user.views.home.MainActivity


class TaskFragment : Fragment(), OnTaskSelected  {


    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskListProgress: MutableList<TaskModel>
    private lateinit var taskListCompleted: MutableList<TaskModel>
    private lateinit var progressAdapter: TaskAdapter
    private lateinit var completedAdapter: TaskAdapter
    private lateinit var completedPaginationScrollListener: PaginationScrollListener
    private lateinit var progressPaginationScrollListener: PaginationScrollListener

   private val viewModel  = TaskViewModel()
    private var selectedPosition  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         resetVariables()
        progressAdapter = TaskAdapter(taskListProgress, this)
        completedAdapter = TaskAdapter(taskListCompleted, this)

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
        viewModel.responseError.observe(this, Observer {
            showSnackBar(it,Color.RED)
        })


    }

    private fun resetVariables() {
        viewModel.pendingPage = 0
        viewModel.completedPage = 0

        taskListProgress = mutableListOf()
        taskListCompleted = mutableListOf()

        viewModel.isLastPageOfCompleted = false
        viewModel.isLastPageOfPending = false

        viewModel.getPendingUserTask()
        viewModel.getCompletedUserTask()


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
                    return viewModel.LoadingPendingTask.get()!!
                }

                override fun loadMoreItems() {

                    if (!viewModel.isLastPageOfPending) {
                        viewModel.pendingPage += Constants.PAGE_SIZE
                        viewModel.getPendingUserTask()
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
                        viewModel.getCompletedUserTask()
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("sachinTASKfRAGMENT","ON vIEW cREATEDE vIEW")
    }
    override fun onDestroyView() {
        super.onDestroyView()
       Log.d("sachin","Ondestroy vIEW")


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("sachin","OnDestroy")

    }


    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
        if (Constants.changedSize!=0){
            resetVariables()
            Constants.changedSize = 0
        }
    }

    override fun onTaskSelected(taskModel: TaskModel , position: Int) {
        selectedPosition = position
        val action = TaskFragmentDirections.taskFragmentToTaskDetailFragment(taskModel)
        findNavController().navigate(action)

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