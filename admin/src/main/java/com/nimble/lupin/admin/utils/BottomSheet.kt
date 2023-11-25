package com.nimble.lupin.admin.utils

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.nimble.lupin.admin.models.UserModel
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Response


class BottomSheet(
    private var list: MutableList<BottomSheetModel>, private var onBottomSheetItemSelected:
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
                                getUsersList()
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
                    getUsersList()
                }
            }
        }
    }


    fun getUsersList() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
        userCall?.cancel()
        userCall = apiService.getAllUserList(page, "")
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
                                        it.firstName + " " + it.lastName
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
    private fun showSnackBar(message: String) {
        val snackBar =  Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(Color.RED)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()

    }


}