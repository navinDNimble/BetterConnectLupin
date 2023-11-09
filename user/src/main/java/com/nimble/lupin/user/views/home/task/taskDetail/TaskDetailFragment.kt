package com.nimble.lupin.user.views.home.task.taskDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nimble.lupin.user.R
import com.nimble.lupin.user.databinding.FragmentTaskDetailBinding
import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.views.home.MainActivity


class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val task = arguments?.getParcelable<TaskModel>("TaskDetail")
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        binding.includedLayout.textViewAssignTaskTaskTitleIn.text = task?.taskName
        binding.includedLayout.textViewAssignTaskStartDateIn.text = getString(R.string.date_combine_string,task?.startDate,task?.endDate)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()

    }

}