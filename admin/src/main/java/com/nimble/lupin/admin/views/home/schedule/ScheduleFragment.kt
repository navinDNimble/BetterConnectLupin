package com.nimble.lupin.admin.views.home.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nimble.lupin.admin.databinding.FragmentScheduleBinding
import com.nimble.lupin.admin.utils.PaginationScrollListener
import com.nimble.lupin.admin.views.home.MainActivity


class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null

    private val binding get() = _binding!!
    private lateinit var paginationScrollListener: PaginationScrollListener
    private var isLastPage  = false
    private var isLoading =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        paginationScrollListener = object :
            PaginationScrollListener(binding.adminAllTaskRecyclerView.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if (isLastPage) {
                    isLoading = true
                    page += Constant.PAGE_SIZE
                    patientsViewModel.patientListRequest?.searchKeyword =
                        getDataBinding().lytSearchPatient.edtSearch?.text.toString()
                    patientsViewModel.callPatientListApi(page = page)
                }
                else
                {
                    isLastPage = true
                }
            }
        }
        binding.assignTaskScheduleButtonId.setOnClickListener {
            ScheduleFragmentDirections.scheduleFragmentToAssignTaskFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}