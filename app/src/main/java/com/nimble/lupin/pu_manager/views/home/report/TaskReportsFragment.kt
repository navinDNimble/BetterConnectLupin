package com.nimble.lupin.pu_manager.views.home.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nimble.lupin.pu_manager.R
import com.nimble.lupin.pu_manager.databinding.FragmentTaskReportsBinding

class TaskReportsFragment : Fragment() {
    private lateinit var binding : FragmentTaskReportsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_reports, container, false)
    }

}