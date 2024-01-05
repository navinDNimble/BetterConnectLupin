package com.nimble.lupin.pu_manager.views.home.report

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.pu_manager.adapters.ReportTaskAdapter
import com.nimble.lupin.pu_manager.api.ApiService
import com.nimble.lupin.pu_manager.api.ResponseHandler
import com.nimble.lupin.pu_manager.databinding.FragmentTaskReportBinding
import com.nimble.lupin.pu_manager.interfaces.OnTaskSelected
import com.nimble.lupin.pu_manager.models.TaskModel
import com.nimble.lupin.pu_manager.utils.Constants
import com.nimble.lupin.pu_manager.utils.PaginationScrollListener
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Response

abstract class ReportBaseFragment : Fragment(), OnTaskSelected {
    lateinit var binding: FragmentTaskReportBinding
    val adapter = ReportTaskAdapter(this)
    val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    private lateinit var paginationScrollListener: PaginationScrollListener
    var isLastPage = false
    var page = 0
    var isLoading = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskReportBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reportCompletedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.reportCompletedRecyclerView.adapter = adapter

        paginationScrollListener = object :
            PaginationScrollListener(binding.reportCompletedRecyclerView.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if (isLastPage.not()) {
                    page += Constants.PAGE_SIZE
                    Log.d("sachin", "Calling By Pagination")
                    getTaskList()
                }
            }
        }

        paginationScrollListener.let { progressPaginationScrollListener ->
            binding.reportCompletedRecyclerView.addOnScrollListener(
                progressPaginationScrollListener
            )
        }

    }

    override fun onTaskSelected(taskModel: TaskModel) {
        val action = ReportFragmentDirections.completedTaskFragmentToTaskDetailFragment(taskModel)
        findNavController().navigate(action)
    }

    abstract fun getTaskList()
    fun showSnackBar(message: String) {
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


    fun handleApiResult(apiCall: Call<ResponseHandler<List<TaskModel>>>) {
        binding.progressBarTask.visibility = View.VISIBLE
        isLoading = true
        apiCall.enqueue(object : retrofit2.Callback<ResponseHandler<List<TaskModel>>> {
            override fun onResponse(
                call: Call<ResponseHandler<List<TaskModel>>>,
                response: Response<ResponseHandler<List<TaskModel>>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("sachinAdminTASK", result.toString())
                    binding.noTaskAvailable.visibility = View.GONE
                    when (result?.code) {
                        200 -> {
                            isLastPage = result.isLastPage
                            val responseList = result.response
                            if (page == 0) {
                                adapter.itemList.clear()
                                adapter.itemList.addAll(responseList)
                                adapter.notifyDataSetChanged()
                            } else {
                                adapter.itemList.addAll(responseList)
                                adapter.notifyItemRangeInserted(
                                    adapter.itemCount,
                                    responseList.size
                                )
                            }

                        }

                        404 -> {
                            isLastPage = result.isLastPage
                            binding.noTaskAvailable.text = result.message
                            binding.noTaskAvailable.visibility = View.VISIBLE
                        }

                        409 -> {
                            Log.d("sachinAdminTASK", result.toString())
                            isLastPage = result.isLastPage
                        }

                        500 -> {

                            showSnackBar("Error in Loading" + result.message)
                        }
                    }

                }
                binding.progressBarTask.visibility = View.GONE
                isLoading = false
            }

            override fun onFailure(call: Call<ResponseHandler<List<TaskModel>>>, t: Throwable) {
                Log.d("sachinscheduletask", t.message.toString())
                binding.progressBarTask.visibility = View.GONE
                showSnackBar(t.message.toString())
                isLoading = false
            }

        })
    }
}
