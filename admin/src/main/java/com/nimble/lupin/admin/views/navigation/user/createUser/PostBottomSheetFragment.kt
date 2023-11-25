package com.nimble.lupin.admin.views.navigation.user.createUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nimble.lupin.admin.adapters.TaskAdapter
import com.nimble.lupin.admin.databinding.FragmentPostBottomSheetBinding
import com.nimble.lupin.admin.interfaces.OnActivitySelected
import com.nimble.lupin.admin.interfaces.OnPostSelected
import com.nimble.lupin.admin.interfaces.OnReportAuthoritySelected
import com.nimble.lupin.admin.interfaces.OnSubActivitySelected
import com.nimble.lupin.admin.interfaces.OnTaskSelected
import com.nimble.lupin.admin.interfaces.OnTrainingModeSelected

import com.nimble.lupin.admin.models.TaskModel
import java.io.InvalidClassException
import java.lang.NullPointerException


class PostBottomSheetFragment<T, U>(private var myList: List<T>, private var callback: U) :
    BottomSheetDialogFragment() {
    private var _binding: FragmentPostBottomSheetBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        //Determining the type of interface to pass list to related adapter
        when (callback) {
            is OnTaskSelected -> {
                // Handle OnTaskSelected
                binding.recyclerView.adapter = TaskAdapter(
                    myList.map { it as TaskModel }, callback as OnTaskSelected
                )
            }



            is OnPostSelected -> {
                // Handle OnPostSelected
            }

            is OnReportAuthoritySelected -> {
                // Handle OnReportAuthoritySelected
            }

            is OnActivitySelected -> {
                // Handle OnActivitySelected
            }

            is OnSubActivitySelected -> {
                // Handle OnSubActivitySelected
            }

            is OnTrainingModeSelected -> {
                // Handle OnTrainingModeSelected
            }

            else -> {
                throw NullPointerException("No interface Found")
                // Handle the case when callback doesn't match any of the specified types
            }
        }


    }
}