package com.nimble.lupin.admin.views.home.schedule.assignTask

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.databinding.FragmentAssignTaskBinding
import com.nimble.lupin.admin.databinding.FragmentCreateTaskBinding
import com.nimble.lupin.admin.databinding.FragmentCreateUserBinding


class AssignTaskFragment : Fragment() {

    private lateinit var binding: FragmentAssignTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssignTaskBinding.inflate(inflater, container, false)

        return binding.root
    }


    private fun isEmpty(userInputValue: String): Boolean {
        if (TextUtils.isEmpty(userInputValue)) {
            return true
        }
        return false
    }

}