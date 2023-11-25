package com.nimble.lupin.admin.views.navigation.createtask

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nimble.lupin.admin.databinding.FragmentCreateTaskBinding
import com.nimble.lupin.admin.views.home.MainActivity


class CreateTaskFragment : Fragment() {

    private lateinit var binding: FragmentCreateTaskBinding  // Declare an instance of your binding class

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);



        binding.createTaskButton.setOnClickListener() {
            val taskTitle = binding.textViewTaskTitle.text.toString()
            val date = binding.textViewDate.text.toString()

            if (isEmpty(taskTitle)) {
                binding.textViewTaskTitle.error = "Task Title Should Not Be Empty"
                binding.textViewTaskTitle.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(date)) {
                binding.textViewDate.error = "Select Date "
                binding.textViewDate.requestFocus()
                return@setOnClickListener
            }
            //TODO:  call create Task Api
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
    }

    private fun isEmpty(userInputValue: String): Boolean {
        if (TextUtils.isEmpty(userInputValue)) {
            return true
        }
        return false
    }

}