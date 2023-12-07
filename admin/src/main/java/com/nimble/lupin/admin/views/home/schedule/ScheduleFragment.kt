package com.nimble.lupin.admin.views.home.schedule

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.adapters.TaskAdapter
import com.nimble.lupin.admin.databinding.FragmentScheduleBinding
import com.nimble.lupin.admin.interfaces.OnTaskSelected
import com.nimble.lupin.admin.models.TaskModel
import com.nimble.lupin.admin.utils.Constants
import com.nimble.lupin.admin.utils.PaginationScrollListener
import com.nimble.lupin.admin.views.home.MainActivity


class ScheduleFragment : Fragment() , OnTaskSelected {

    private var _binding: FragmentScheduleBinding? = null
    private var scheduleViewModel: ScheduleViewModel? = null
    private val binding get() = _binding!!
    private lateinit var paginationScrollListener: PaginationScrollListener

    private lateinit var adapter: TaskAdapter

    private lateinit var taskList: MutableList<TaskModel>
    private val handler = Handler(Looper.getMainLooper())
    private var searchDelayMillis = 500L

    private val textListener =  object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(searchText: String?): Boolean {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                scheduleViewModel?.page = 0
                scheduleViewModel?.searchKey = searchText.toString()
                scheduleViewModel!!.getTaskList()
            }, searchDelayMillis)

            return true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleViewModel = ScheduleViewModel()
        scheduleViewModel!!.responseError.observe(this) {
            showSnackBar(it)
        }
        scheduleViewModel!!.page = 0
        scheduleViewModel!!.isLastPage =false
        taskList = mutableListOf()
        adapter = TaskAdapter(taskList, this)
        scheduleViewModel!!.taskListResponse.observe(this, Observer {
            if (scheduleViewModel!!.page==0){
                taskList.clear()
            }
            Log.d("saching task",it.toString())

            taskList.addAll(it)
            Log.d("saching task",taskList.size.toString())
            adapter.updateList(taskList)
            adapter.notifyDataSetChanged()
        })
        scheduleViewModel!!.loadingProgressBar.observe(this, Observer {
            if (it){
                binding.progressBarTask.visibility = View.VISIBLE
            }else{
                binding.progressBarTask.visibility = View.GONE
            }
        })
        scheduleViewModel!!.getTaskList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.adminAllTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.adminAllTaskRecyclerView.adapter = adapter
        paginationScrollListener = object : PaginationScrollListener(binding.adminAllTaskRecyclerView.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return scheduleViewModel!!.isLastPage
            }

            override fun isLoading(): Boolean {
                return scheduleViewModel!!.loadingProgressBar.value!!
            }

            override fun loadMoreItems() {
                if (scheduleViewModel!!.isLastPage.not()) {
                    scheduleViewModel!!.page += Constants.PAGE_SIZE
                    scheduleViewModel!!.getTaskList()
                }
            }
        }

        paginationScrollListener.let { progressPaginationScrollListener ->
            binding.adminAllTaskRecyclerView.addOnScrollListener(
                progressPaginationScrollListener
            )
        }

        binding.assignTaskScheduleButtonId.setOnClickListener {
            ScheduleFragmentDirections.scheduleFragmentToAssignTaskFragment()
        }
        binding.assignTaskScheduleButtonId.setOnClickListener { findNavController().navigate(R.id.scheduleFragment_to_AssignTaskFragment) }
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
        binding.searchTaskView.setOnQueryTextListener(textListener)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
    override fun onStop() {
        super.onStop()
        binding.searchTaskView.setOnQueryTextListener(null)
    }
    override fun onTaskSelected(taskModel: TaskModel) {
        val action = ScheduleFragmentDirections.scheduleFragmentToTaskDetailFragment(taskModel)
        findNavController().navigate(action)
    }
}