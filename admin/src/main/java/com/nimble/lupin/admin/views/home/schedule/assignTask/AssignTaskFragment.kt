package com.nimble.lupin.admin.views.home.schedule.assignTask


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.adapters.UsersSelectionAdapter
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.databinding.FragmentAssignTaskBinding
import com.nimble.lupin.admin.databinding.FragmentUserSelectionBinding
import com.nimble.lupin.admin.interfaces.OnBottomSheetItemSelected
import com.nimble.lupin.admin.models.BottomSheetModel
import com.nimble.lupin.admin.models.UserModel
import com.nimble.lupin.admin.utils.BottomSheet
import com.nimble.lupin.admin.utils.Constants
import com.nimble.lupin.admin.utils.PaginationScrollListener
import com.nimble.lupin.admin.views.home.MainActivity
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Response


class AssignTaskFragment : Fragment(), OnBottomSheetItemSelected {

    private lateinit var binding: FragmentAssignTaskBinding
    private lateinit var taskBottomSheet: BottomSheet
    private var selectedTaskId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssignTaskBinding.inflate(inflater, container, false)
        taskBottomSheet = BottomSheet(mutableListOf(), this, "task", true, requireContext())
        binding.taskNameTextView.setOnClickListener {
            taskBottomSheet.show()
        }
        binding.selectFiledFacilitator.setOnClickListener {
            showUserDialog()
        }
        return binding.root
    }

    private fun showUserDialog() {

        val userSelectionDialog = context?.let { UserSelectionListDialog(requireContext()) }
        userSelectionDialog?.show()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
    }

    override fun onBottomSheetItemSelected(bottomSheetItem: BottomSheetModel, type: String) {
        when (type) {

            "task" -> {
                val selectedtask = taskBottomSheet.getAdapterList().find {
                    it.taskId == bottomSheetItem.id
                }
                binding.taskNameTextView.text = bottomSheetItem.title
                selectedTaskId = bottomSheetItem.id
                binding.activityTextView.text = selectedtask?.activityName
                binding.subActivityTextView.text = selectedtask?.subActivityName
                binding.modeOfTraining.text = selectedtask?.modeName
                binding.dateTextView.text = getString(
                    R.string.date_combine_string,
                    selectedtask?.startDate,
                    selectedtask?.endDate
                )
                taskBottomSheet.cancel()
            }
        }
    }


     fun showSnackBar(message: String) {
        val snackBar =  Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(Color.RED)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()

    }
}
class UserSelectionListDialog(context :Context) : Dialog(context) {
    private var _binding: FragmentUserSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var paginationScrollListener: PaginationScrollListener
    private val userList = mutableListOf<UserModel>()
    private val adapter = UsersSelectionAdapter(userList)
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var page: Int = 0
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    private val searchKey: String = ""
    private var userCall: Call<ResponseHandler<List<UserModel>>>? = null

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.setOnShowListener {
//            val bottomSheetDialog = it as BottomSheetDialog
//            val parentLayout = bottomSheetDialog.findViewById<View>(
//                com.google.android.material.R.id.design_bottom_sheet
//            )
//            parentLayout?.let { bottomSheet ->
//                val behaviour = BottomSheetBehavior.from(bottomSheet)
//                val layoutParams = bottomSheet.layoutParams
//                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
//                bottomSheet.layoutParams = layoutParams
//                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
//            }
//        }
//        return dialog
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = FragmentUserSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerViewUser.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewUser.adapter = adapter
        paginationScrollListener = object :
            PaginationScrollListener(binding.recyclerViewUser.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return isLoading
            }

            override fun isLoading(): Boolean {
                return isLastPage
            }

            override fun loadMoreItems() {
                Log.d("sachin", isLastPage.toString())
                if (isLastPage.not()) {
                    page += Constants.PAGE_SIZE
                    getUsersList()
                }

            }
        }
        paginationScrollListener.let { progressPaginationScrollListener ->
            binding.recyclerViewUser.addOnScrollListener(
                progressPaginationScrollListener
            )
        }

        getUsersList()

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
                            userList.addAll(apiList)
                            adapter.updateList(userList)
                            Log.d("sachinAdminTASK", userList.toString())
                            adapter.notifyDataSetChanged()
                        }

                        404 -> {
                            isLastPage = result.isLastPage
//                            showSnackBar("No Users Available" + result.message)

                        }

                        409 -> {
                            isLastPage = result.isLastPage
                        }

                        500 -> {
//                            showSnackBar("Error in Loading Users" + result.message)

                        }
                    }
                }
                binding.progressBar.visibility= View.GONE
                isLoading = false
            }

            override fun onFailure(call: Call<ResponseHandler<List<UserModel>>>, t: Throwable) {

                binding.progressBar.visibility= View.GONE
//                showSnackBar("Error in Loading Users" + t.message.toString())
                isLoading = false
            }
        })

    }

}