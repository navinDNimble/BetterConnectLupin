package com.nimble.lupin.admin.views.home.schedule.assignTask


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.adapters.UsersSelectionAdapter
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.databinding.FragmentAssignTaskBinding
import com.nimble.lupin.admin.interfaces.OnBottomSheetItemSelected
import com.nimble.lupin.admin.models.AssignTaskBody
import com.nimble.lupin.admin.models.AssignTaskModel
import com.nimble.lupin.admin.models.BottomSheetModel
import com.nimble.lupin.admin.models.UserModel
import com.nimble.lupin.admin.utils.BottomSheet
import com.nimble.lupin.admin.utils.Constants
import com.nimble.lupin.admin.utils.PaginationScrollListener
import com.nimble.lupin.admin.views.home.MainActivity
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AssignTaskFragment : Fragment(), OnBottomSheetItemSelected {

    private lateinit var binding: FragmentAssignTaskBinding
    private lateinit var taskBottomSheet: BottomSheet
    private var selectedTaskId: Int = 0


    private  lateinit var paginationScrollListener: PaginationScrollListener


    val userList = mutableListOf<AssignTaskModel>()

    var adapter  :UsersSelectionAdapter? = UsersSelectionAdapter(userList)
    var isLoading: Boolean = false
    var isLastPage: Boolean = false
    var page: Int = 0
    val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    var searchKey: String = ""
    var userCall: Call<ResponseHandler<List<AssignTaskModel>>>? = null
    private val handler = Handler(Looper.getMainLooper())
    private var searchDelayMillis = 500L  // 500 milliseconds
    companion object{
        var selectedItemList = mutableSetOf<AssignTaskModel>()
    }

    private   var  userModel  : UserModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentAssignTaskBinding.inflate(inflater, container, false)
        selectedItemList.clear()
        taskBottomSheet = BottomSheet(mutableListOf(), this, "task", true, requireContext())
        userModel = arguments?.getParcelable("UserDetail")
        if (userModel!=null){
            binding.selectFiledFacilitator.isEnabled = false
            binding.editTextUnits.visibility =View.VISIBLE
            binding.selectFiledFacilitator.text = userModel!!.firstName + " " + userModel!!.lastName
        }
        binding.taskNameTextView.setOnClickListener {
            taskBottomSheet.show()
        }
        binding.selectFiledFacilitator.setOnClickListener {
            changeVisibility(false)
        }
        binding.backButtonMain.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        initializeUserSelectListDialog()
        return binding.root
    }

    private fun assignTask() {

       if (selectedTaskId == 0){
           binding.taskNameTextView.requestFocus()
           binding.taskNameTextView.error = "Please Select a task"
           return
       }
        if (userModel!=null){
            val singleUserUnit =  binding.editTextUnits.text
            if (singleUserUnit.isNullOrEmpty() || singleUserUnit.toString().toInt() ==0){
                   binding.editTextUnits.requestFocus()
                   binding.editTextUnits.setError("Enter Units")

                   return
            }
            selectedItemList.add(AssignTaskModel(userModel!!.userId,"","" ,singleUserUnit.toString(),true))
        }
       if (selectedItemList.isEmpty()) {
           binding.selectFiledFacilitator.requestFocus()
           binding.selectFiledFacilitator.error = "Please Select User"
           return
       }

        for (item in selectedItemList) {
            if (item.total_units.isNullOrEmpty()){
                showSnackBar("Unit Not Assigned to "+item.firstName+" - " + item.lastName,Color.RED)
                return
            }
        }
        Log.d("sachin2", selectedItemList.toString())
       val assign = AssignTaskBody(selectedTaskId, selectedItemList)
        binding.allotTaskProgress.visibility =View.VISIBLE
        binding.assignTaskButton.visibility =View.GONE
        apiService.allotTaskToUser(assign).enqueue(object : Callback<ResponseHandler<AssignTaskBody>> {
            override fun onResponse(
                call: Call<ResponseHandler<AssignTaskBody>>,
                response: Response<ResponseHandler<AssignTaskBody>>
            ) {
                val result = response.body()
                if (result != null) {
                    Log.d("sachinAssignTask",result.toString())
                    if (result.code==200){
                        showSnackBar(result.message ,Color.GREEN)
                        Log.d("sachin", "SUCCESS")

                        fragmentManager?.popBackStack()
                        Constants.isChanged = true
                    }else{
                        showSnackBar(result.message , Color.RED)
                        Log.d("sachinAssignTask", result.message)
                    }
                }

            }

            override fun onFailure(call: Call<ResponseHandler<AssignTaskBody>>, t: Throwable) {
                t.message?.let { showSnackBar(it , Color.RED) }
                Log.d("sachin", t.message.toString())
                binding.assignTaskButton.visibility =View.VISIBLE
                binding.allotTaskProgress.visibility =View.GONE
            }

        })


    }

    private fun changeVisibility(setInputDetailVisible :Boolean ){
       binding.inputDetailView.visibility = if (setInputDetailVisible) View.VISIBLE else View.GONE
       binding.multiuserSelectionView.visibility = if (setInputDetailVisible) View.GONE else View.VISIBLE

   }
    fun setTextOnFiledFacilitator(){
        val selectedList = adapter?.getSelectedList()
        selectedItemList.addAll(selectedList!!)
        val size = selectedItemList.size.toString()
        Log.d("sachin",size.toString())
        if (selectedItemList.size==0){
            binding.selectFiledFacilitator.text=""
        }else if (selectedItemList.size==1){
            binding.selectFiledFacilitator.text= selectedItemList.first().firstName + " "+ selectedItemList.first().lastName
        }
        else if (selectedItemList.size>1){
            binding.selectFiledFacilitator.text= "Multiple User Selected"
        }
        changeVisibility(true)
    }
    private fun initializeUserSelectListDialog() {
        binding.assignTaskButton.setOnClickListener {
            assignTask()
        }

        binding.backButton.setOnClickListener {

            setTextOnFiledFacilitator()
        }
        binding.continueButton.setOnClickListener {
            setTextOnFiledFacilitator()
        }
        binding.searchViewUserList.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                   page= 0
                    searchKey = newText.toString()
                    getUsersList()
                }, searchDelayMillis)

                return true

            }

        })

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
        userCall = apiService.getAllUserForSelectionList(page, searchKey)
        userCall?.enqueue(object : Callback<ResponseHandler<List<AssignTaskModel>>> {
            override fun onResponse(
                call: Call<ResponseHandler<List<AssignTaskModel>>>,
                response: Response<ResponseHandler<List<AssignTaskModel>>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("sachinAdminTASK", result.toString())
                    when (result?.code) {
                        200 -> {
                            isLastPage = result.isLastPage
                            val responseList = result.response
                            val selectedList = adapter?.getSelectedList()
                            selectedItemList.addAll(selectedList!!)
                            Log.d("sachinAdminTASK", selectedItemList.toString())
                            selectedItemList.forEach { item1 ->
                                responseList.find { it.userId == item1.userId }?.let { matchingItem ->
                                    matchingItem.total_units = item1.total_units
                                    matchingItem.isSelected = true
                                    Log.d("sachinMatching",item1.toString())
                                }
                            }

                            if (page ==0){
                                Log.d("sachinMatching",page.toString())
                                userList.clear()
                                userList.addAll(responseList)
                                binding.recyclerViewUser.adapter = null
                                adapter =null
                                adapter = UsersSelectionAdapter(userList)
                                binding.recyclerViewUser.adapter = adapter
                            }else{
                               val size = userList.size
                                userList.addAll(responseList)
                                adapter?.notifyItemRangeInserted(size , responseList.size)
                            }

                        }

                        404 -> {
                            isLastPage = result.isLastPage
                            showSnackBar("No Users Available" + result.message  , Color.RED)

                        }

                        409 -> {
                            isLastPage = result.isLastPage
                        }

                        500 -> {
                            showSnackBar("Error in Loading Users" + result.message  , Color.RED)

                        }
                    }
                }
                binding.progressBar.visibility= View.GONE
                isLoading = false
            }

            override fun onFailure(call: Call<ResponseHandler<List<AssignTaskModel>>>, t: Throwable) {

                binding.progressBar.visibility= View.GONE
                showSnackBar("Error in Loading Users" + t.message.toString() , Color.RED)
                isLoading = false
            }
        })

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
                Log.d("sachinMatching",bottomSheetItem.toString())
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


     fun showSnackBar(message: String  ,color :Int) {
        val snackBar =  Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(color)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()

    }





}

