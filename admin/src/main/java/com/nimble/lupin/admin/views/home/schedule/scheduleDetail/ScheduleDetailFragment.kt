package com.nimble.lupin.admin.views.home.schedule.scheduleDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nimble.lupin.admin.databinding.FragmentScheduleDetailBinding
import com.nimble.lupin.admin.models.TaskModel
import com.nimble.lupin.admin.views.home.MainActivity


class ScheduleDetailFragment : Fragment() {
    private var _binding: FragmentScheduleDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val task = arguments?.getParcelable<TaskModel>("TaskDetail")
        _binding = FragmentScheduleDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
    }
}