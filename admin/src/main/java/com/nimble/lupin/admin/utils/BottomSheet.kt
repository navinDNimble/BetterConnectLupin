package com.nimble.lupin.admin.utils

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.adapters.BottomAdapter
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.databinding.ActivityLoginBinding
import com.nimble.lupin.admin.models.BottomSheetModel
import com.nimble.lupin.admin.databinding.FragmentPostBottomSheetBinding
import com.nimble.lupin.admin.interfaces.OnBottomSheetItemSelected
import com.nimble.lupin.admin.models.TaskModel
import com.nimble.lupin.admin.models.UserModel
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Response


class BottomSheet(
    private var list: MutableList<BottomSheetModel>, onBottomSheetItemSelected:
    OnBottomSheetItemSelected, private val type: String, private val isPagination: Boolean,
    context :Context
) :
    BottomSheetDialog(context) {
    private var _binding: FragmentPostBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var paginationScrollListener: PaginationScrollListener

    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var page: Int = 0
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    private val searchKey: String = ""
    private var userCall: Call<ResponseHandler<List<UserModel>>>? = null

    private var taskCall: Call<ResponseHandler<List<TaskModel>>>? = null
    private var taskList : MutableList<TaskModel> = mutableListOf()

    private val adapter = BottomAdapter(list, onBottomSheetItemSelected, type)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentPostBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        if (isPagination) {

            paginationScrollListener = object :
                PaginationScrollListener(binding.recyclerView.layoutManager as LinearLayoutManager) {

                override fun isLastPage(): Boolean {
                    return isLoading
                }

                override fun isLoading(): Boolean {
                    return isLastPage
                }

                override fun loadMoreItems() {
                    Log.d("sachin",isLastPage.toString())
                    if (isLastPage.not()) {
                        page += Constants.PAGE_SIZE
                        when (type) {
                            "authority" -> {
                                getAuthorityList()
                            }
                            "task" -> {
                                getTasksList()
                            }
                        }
                    }

                }
            }
            paginationScrollListener.let { progressPaginationScrollListener ->
                binding.recyclerView.addOnScrollListener(
                    progressPaginationScrollListener
                )
            }

            when (type) {

                "authority" -> {
                    getAuthorityList()
                }
                "task" -> {
                    Log.d("sachin","getTaskList Called")
                    getTasksList()
                }
                else -> {
                    Log.d("sachin","getTaskList Called")
                }
            }
        }
    }

    public fun getAdapterList(): MutableList<TaskModel> {
        return taskList
    }
   public fun updateList(  newList: MutableList<BottomSheetModel>){
       list = newList
       adapter.notifyDataSetChanged()
   }

    fun getAuthorityList() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
        userCall?.cancel()
        userCall = apiService.getAllAuthority()
        userCall?.enqueue(object : retrofit2.Callback<ResponseHandler<List<UserModel>>> {
            override fun onResponse(
                call: Call<ResponseHandler<List<UserModel>>>,
                response: Response<ResponseHandler<List<UserModel>>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("sachinAdminTASK", result.toString())
                    when (result?.code) {
                        200 -> {
                            isLastPage = result.isLastPage
                            val apiList = result.response
                            apiList.forEach {
                                list.add(
                                    BottomSheetModel(
                                        it.userId,
                                        it.firstName + " " + it.lastName + " -" + it.workStation
                                    )
                                )
                            }
                            adapter.notifyDataSetChanged()
                        }
                        404 -> {
                            isLastPage = result.isLastPage
                            showSnackBar("No Users Available" + result.message)

                        }

                        409 -> {
                            isLastPage = result.isLastPage
                        }

                        500 -> {
                            showSnackBar("Error in Loading Users" + result.message)
                        }
                    }
                }
                binding.progressBar.visibility= View.GONE
                isLoading = false
            }

            override fun onFailure(call: Call<ResponseHandler<List<UserModel>>>, t: Throwable) {

                binding.progressBar.visibility= View.GONE
                showSnackBar("Error in Loading Users" + t.message.toString())
                isLoading = false
            }
        })

    }


    fun getTasksList(){
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
        taskCall?.cancel()
        taskCall = apiService.getTaskToAssign(page, searchKey)
        taskCall?.enqueue(object : retrofit2.Callback<ResponseHandler<List<TaskModel>>> {
            override fun onResponse(
                call: Call<ResponseHandler<List<TaskModel>>>,
                response: Response<ResponseHandler<List<TaskModel>>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()

                    when (result?.code) {
                        200 -> {
                            isLastPage = result.isLastPage
                            val apiList = result.response
                            taskList.addAll(apiList)
                            apiList.forEach {
                                list.add(
                                    BottomSheetModel(
                                        it.taskId,
                                        it.taskName.toString()
                                    )
                                )
                            }
                            adapter.notifyDataSetChanged()
                        }

                        404 -> {
                            isLastPage = result.isLastPage
                            showSnackBar("No Users Available" + result.message)

                        }

                        409 -> {
                            isLastPage = result.isLastPage
                        }

                        500 -> {
                            showSnackBar("Error in Loading Users" + result.message)

                        }
                    }
                }
                binding.progressBar.visibility= View.GONE
                isLoading = false
            }

            override fun onFailure(call: Call<ResponseHandler<List<TaskModel>>>, t: Throwable) {

                binding.progressBar.visibility= View.GONE
                showSnackBar("Error in Loading Users" + t.message.toString())
                isLoading = false
            }
        })
    }
    private fun showSnackBar(message: String) {
        val snackBar =  Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(Color.RED)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()

    }


}