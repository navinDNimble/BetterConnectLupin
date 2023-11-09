package com.nimble.lupin.user.views.home.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nimble.lupin.user.R
import com.nimble.lupin.user.adapters.TaskAdapter
import com.nimble.lupin.user.databinding.FragmentTaskBinding
import com.nimble.lupin.user.interfaces.OnTaskSelected
import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.views.home.MainActivity

class TaskFragment : Fragment() ,OnTaskSelected {

    private lateinit var viewModel: TaskViewModel
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private  lateinit var  taskListProgress :MutableList<TaskModel>
    private  lateinit var  taskListCompleted :MutableList<TaskModel>
    private lateinit var progressAdapter:TaskAdapter
    private lateinit var completedAdapter:TaskAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressTaskRecyclerView.adapter =progressAdapter
        binding.completedTaskRecyclerView.adapter = completedAdapter

        binding.progressSelectionView.setOnClickListener {
            //false : Visibility Gone
            if (viewModel.progressRecyclerVisibility.get() ==true){
                viewModel.progressRecyclerVisibility.set(false)
                binding.progressSelectionView.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.arrow_drop_down_circle,
                    0
                )
            }else{
                viewModel.progressRecyclerVisibility.set(true)
                binding.progressSelectionView.setCompoundDrawablesWithIntrinsicBounds(
                   0,
                    0,
                    R.drawable.arrow_drop_up,
                    0
                )
                viewModel.completedRecyclerVisibility.set(false)
                binding.completedSelectionView.setCompoundDrawablesWithIntrinsicBounds(
                   0,
                    0,
                    R.drawable.arrow_drop_down_circle,
                    0
                )
            }
        }
        binding.completedSelectionView.setOnClickListener {
            if (viewModel.completedRecyclerVisibility.get() ==true){
                viewModel.completedRecyclerVisibility.set(false)
                binding.completedSelectionView.setCompoundDrawablesWithIntrinsicBounds(
                   0,
                    0,
                    R.drawable.arrow_drop_down_circle,
                    0
                )
            }else{
                viewModel.completedRecyclerVisibility.set(true)
                binding.completedSelectionView.setCompoundDrawablesWithIntrinsicBounds(
                   0,
                    0,
                    R.drawable.arrow_drop_up,
                    0
                )
                viewModel.progressRecyclerVisibility.set(false)
                binding.progressSelectionView.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.arrow_drop_down_circle,
                    0
                )
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
        refineList()
    }

    private fun refineList() {
        val iterator = taskListProgress.iterator()
        while (iterator.hasNext()) {
            val it = iterator.next()
            if (it.completedUnits >= it.TotalUnits) {
                taskListCompleted.add(it)
                iterator.remove()
                progressAdapter.updateList(taskListProgress)
                completedAdapter.updateList(taskListCompleted)
                break
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        taskListProgress = mutableListOf()
        progressAdapter = TaskAdapter(taskListProgress,this)

        taskListCompleted = mutableListOf()
        completedAdapter = TaskAdapter(taskListCompleted , this)


        viewModel.taskList.observe(this) { it ->
            it.forEach {
                if (it.completedUnits>=it.TotalUnits){
                    taskListCompleted.add(it)
                }else{
                    taskListProgress.add(it)
                }
                completedAdapter.updateList(taskListCompleted)
                progressAdapter.updateList(taskListProgress)
            }
        }
        viewModel.getUserTask(2)

    }

    override fun onTaskSelected(taskModel: TaskModel) {

        val action = TaskFragmentDirections.taskFragmentToTaskDetailFragment(taskModel)
        findNavController().navigate(action)
    }

}