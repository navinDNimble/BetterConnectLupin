package com.nimble.lupin.admin.views.navigation.user.createUser

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.models.BottomSheetModel
import com.nimble.lupin.admin.databinding.FragmentCreateUserBinding
import com.nimble.lupin.admin.interfaces.OnBottomSheetItemSelected
import com.nimble.lupin.admin.models.UserModel
import com.nimble.lupin.admin.utils.BottomSheet
import com.nimble.lupin.admin.views.home.MainActivity
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern


class CreateUserFragment : Fragment() ,OnBottomSheetItemSelected {

    private lateinit var binding: FragmentCreateUserBinding
    private val calendar = Calendar.getInstance()
    private lateinit var  datePickerDialog  : DatePickerDialog
    private lateinit var postBottomSheet  :BottomSheet
    private lateinit var reportAuthorityBottomSheet  :BottomSheet
    private var postId : Int = 5
    private var reportAuthorityId : Int = 0
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateUserBinding.inflate(inflater, container, false)

        val bottomModelList = mutableListOf(
//            BottomSheetModel(2, "Project Manager"),
//            BottomSheetModel(3, "PU Manager"),
//            BottomSheetModel(4, "Project Coordinator"),
            BottomSheetModel(5, "Field Facilitator")
        )

        postBottomSheet = BottomSheet(bottomModelList , this , "post" ,false, requireContext())
        reportAuthorityBottomSheet = BottomSheet(mutableListOf() , this , "authority" ,true,requireContext())

        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                binding.joiningDateId.text
                calendar.set(year, monthOfYear, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate: String = sdf.format(calendar.time)
                binding.joiningDateId.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
         datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        binding.backButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        binding.createUserId.setOnClickListener() {


            val firstName = binding.firstNameId.text.toString()
            val lastName = binding.lastNameId.text.toString()
            val mobileNumber = binding.mobileNumberId.text.toString()
            val emailId = binding.emailIdId.text.toString()
            val workStation = binding.workStationId.text.toString()
            val empIdNumber = binding.empIdNumberId.text.toString()
            val joiningDate = binding.joiningDateId.text.toString()


            if (isEmpty(firstName)) {
                binding.firstNameId.error = "FirstName Should Not Be Empty."
                binding.firstNameId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(lastName)) {
                binding.lastNameId.error = "LastName Should Not Be Empty."
                binding.lastNameId.requestFocus()
                return@setOnClickListener
            }
            if (mobileNumber.length != 10) {
                binding.mobileNumberId.error = " Enter Valid Mobile Number."
                binding.mobileNumberId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(emailId)) {
                binding.emailIdId.error = "Email Address Should Not Be Empty."
                binding.emailIdId.requestFocus()
                return@setOnClickListener
            }
            if (isValidEmail(emailId).not()) {
                binding.emailIdId.error = "Enter a Valid Email Address."
                binding.emailIdId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(workStation)) {
                binding.workStationId.error = "WorkStation Should Not Be Empty."
                binding.workStationId.requestFocus()
                return@setOnClickListener
            }

            if (isEmpty(empIdNumber)) {
                binding.empIdNumberId.error = "Emp Id Number Should Not Be Empty."
                binding.empIdNumberId.requestFocus()
                return@setOnClickListener
            }
            if (reportAuthorityId==0) {
                binding.reportAuthorityId.error = "Select ReportAuthority "
                binding.reportAuthorityId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(joiningDate)) {
                binding.joiningDateId.error = "Joining Date  Should Not Be Empty."
                binding.joiningDateId.requestFocus()
                return@setOnClickListener
            }
            binding.createUserProgressBar.visibility = View.VISIBLE
            binding.createUserId.visibility = View.GONE
            val userModel = UserModel(0,firstName,lastName,mobileNumber, emailId,workStation,postId,empIdNumber.toInt(),reportAuthorityId,joiningDate)
            apiService.createUser(userModel).enqueue(object :Callback<ResponseHandler<UserModel>>{
                override fun onResponse(
                    call: Call<ResponseHandler<UserModel>>,
                    response: Response<ResponseHandler<UserModel>>
                ) {
                    val result = response.body()
                    Log.d("sachin", result?.response.toString())
                    if (result?.code==200){
                        showSnackBar(result.message,Color.GREEN)
                        Log.d("sachin", result.toString())
                        val action  = CreateUserFragmentDirections.createUserFragmentToNavigationUserList()
                        findNavController().navigate(action)
                    }else if (result?.code==409){
                        showSnackBar(result.message,Color.RED)
                    }
                    binding.createUserProgressBar.visibility = View.GONE
                    binding.createUserId.visibility = View.VISIBLE
                }

                override fun onFailure(call: Call<ResponseHandler<UserModel>>, t: Throwable) {
                    showSnackBar(t.message.toString(),Color.RED)
                    binding.createUserProgressBar.visibility = View.GONE
                    binding.createUserId.visibility = View.VISIBLE
                }

            })

        }
        binding.joiningDateId.setOnClickListener() {
            datePickerDialog.show()
        }
        binding.postId.setOnClickListener{
         postBottomSheet.show()
        }
        binding.reportAuthorityId.setOnClickListener{
            reportAuthorityBottomSheet.show()
        }
        return binding.root

    }



    private fun isEmpty(userInputValue: String): Boolean {
        if (TextUtils.isEmpty(userInputValue)) {
            return true
        }
        return false
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
        return Pattern.compile(regex).matcher(email).matches()
    }
    override fun onBottomSheetItemSelected(bottomSheetItem: BottomSheetModel, type: String) {
        when (type) {
            "post" ->{
                binding.postId.text = bottomSheetItem.title
                postId = bottomSheetItem.id
                postBottomSheet.cancel()
            }
            "authority" ->{
                binding.reportAuthorityId.text = bottomSheetItem.title
                reportAuthorityId = bottomSheetItem.id
                reportAuthorityBottomSheet.cancel()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
    }

    private fun showSnackBar(message: String , color :Int) {
        val snackBar =  Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(color)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()

    }
}