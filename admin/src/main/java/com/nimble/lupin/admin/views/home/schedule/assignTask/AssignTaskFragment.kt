package com.nimble.lupin.admin.views.home.schedule.assignTask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.databinding.FragmentAssignTaskBinding


class AssignTaskFragment : Fragment() {

    private var _binding: FragmentAssignTaskBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAssignTaskBinding.inflate(inflater, container, false)
        return binding.root
    }


}