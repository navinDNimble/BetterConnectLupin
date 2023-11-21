package com.nimble.lupin.admin.views.navigation.user.createUser

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nimble.lupin.admin.databinding.FragmentCreateUserBinding
import java.util.regex.Pattern


class CreateUserFragment : Fragment() {

    private lateinit var binding: FragmentCreateUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateUserBinding.inflate(inflater, container, false);

        binding.createUserId.setOnClickListener() {
            val firstName = binding.firstNameId.text.toString()
            val lastName = binding.lastNameId.text.toString()
            val mobileNumber = binding.mobileNumberId.text.toString()
            val emailId = binding.emailIdId.text.toString()
            val workStation = binding.workStationId.text.toString()
            val post = binding.postId.text.toString()
            val empIdNumber = binding.empIdNumberId.text.toString()
            val reportAuthority = binding.reportAuthorityId.text.toString()
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
            if (isValidEmail(emailId)) {
                binding.emailIdId.error = "Enter a Valid Email Address."
                binding.emailIdId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(workStation)) {
                binding.workStationId.error = "WorkStation Should Not Be Empty."
                binding.workStationId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(post)) {
                binding.postId.error = "Post Should Not Be Empty."
                binding.postId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(empIdNumber)) {
                binding.empIdNumberId.error = "Emp Id Number Should Not Be Empty."
                binding.empIdNumberId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(reportAuthority)) {
                binding.reportAuthorityId.error = "Report Authority Should Not Be Empty."
                binding.reportAuthorityId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(joiningDate)) {
                binding.joiningDateId.error = "Joining Date  Should Not Be Empty."
                binding.joiningDateId.requestFocus()
                return@setOnClickListener
            }

        }

        //TODO : Call Api

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




}