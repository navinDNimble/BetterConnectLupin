package com.nimble.lupin.user.views.login

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.nimble.lupin.user.api.ApiService
import com.nimble.lupin.user.api.ResponseHandler
import com.nimble.lupin.user.databinding.ActivityLoginBinding
import com.nimble.lupin.user.models.UserProfileModel
import com.nimble.lupin.user.utils.Constants
import com.nimble.lupin.user.views.home.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var storedVerificationId = ""
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    private lateinit var userProfileModel: UserProfileModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

       mAuth =FirebaseAuth.getInstance()
        binding.sendOtpButton.setOnClickListener {
            val phoneNumber = binding.phoneNumberEditText.text.toString()
            if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length != 10) {
                showSnackBar("Please enter a valid 10-digit mobile number", Color.RED)
                return@setOnClickListener
            }

            binding.sendOtpButton.visibility = View.GONE
            binding.progressBarMobileNumber.visibility = View.VISIBLE

            apiService.checkMobileNumber(phoneNumber)
                .enqueue(object : Callback<ResponseHandler<UserProfileModel>> {
                    override fun onResponse(
                        call: Call<ResponseHandler<UserProfileModel>>,
                        response: Response<ResponseHandler<UserProfileModel>>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                           Log.d("sachinLogin",result.toString())
                            if (result?.code == 200) {
                                userProfileModel = result.response
                                sendOtp(phoneNumber)
                                Log.d("sachinLogin",result.response.toString())
                            } else {
                                binding.sendOtpButton.visibility = View.VISIBLE
                                binding.progressBarMobileNumber.visibility = View.GONE
                                showSnackBar(result?.message.toString(), Color.RED)
                                Log.d("sachinLogin",result.toString())
                            }

                        }else{
                            showSnackBar("Unknown Error" , Color.RED)
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseHandler<UserProfileModel>>,
                        t: Throwable
                    ) {
                        binding.sendOtpButton.visibility = View.VISIBLE
                        binding.progressBarMobileNumber.visibility = View.GONE
                        showSnackBar(t.message.toString(), Color.RED)
                        Log.d("sachinLogin",t.message.toString())
                    }

                })


        }

        binding.verifyOtpButton.setOnClickListener {
            val otpS = binding.pinViewOTP.text.toString()
            if (TextUtils.isEmpty(otpS) || otpS.length != 6) {
                showSnackBar("Enter Valid Otp", Color.RED)
                return@setOnClickListener
            }

            binding.progressBarOtp.visibility = View.VISIBLE
            binding.verifyOtpButton.visibility = View.GONE
            val credentials = PhoneAuthProvider.getCredential(storedVerificationId, otpS)
            signInWithPhoneAuthCredential(credentials)

        }

    }

    private fun sendOtp(phoneNumber: String) {

        val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                showSnackBar("Verification Code Send", Color.GREEN)
                storedVerificationId = verificationId
                resendToken = token
                binding.mobileNumberLayout.visibility = View.GONE
                binding.verifyOtpLayout.visibility = View.VISIBLE
                binding.sendOtpButton.visibility = View.VISIBLE
                binding.progressBarMobileNumber.visibility = View.GONE
            }

            override fun onVerificationFailed(e: FirebaseException) {
                binding.progressBarMobileNumber.visibility = View.GONE
                when (e) {
                    is FirebaseAuthInvalidCredentialsException ->
                        showSnackBar("Invalid Number", Color.RED)

                    is FirebaseTooManyRequestsException ->
                        showSnackBar("Too Many Request Try After Some Time", Color.RED)

                    else ->
                        showSnackBar("Failed" + e.message, Color.RED)
                }
                binding.sendOtpButton.visibility = View.VISIBLE
                binding.progressBarMobileNumber.visibility = View.GONE
            }


        }
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91$phoneNumber")       // Phone number to verify
            .setTimeout(30, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)
                    val sharedPreferences = sharedPref.edit()
                    sharedPreferences.putInt(Constants.User_ID,userProfileModel.userId)
                    sharedPreferences.putString(Constants.User_Name,userProfileModel.firstName+ " "+userProfileModel.lastName)
                    sharedPreferences.putString(Constants.User_Profile,userProfileModel.toString())
                    sharedPreferences.apply()
                    showSnackBar("Log In Success", Color.GREEN)
                    changeActivity()
                } else {
                    binding.progressBarOtp.visibility = View.GONE
                    binding.verifyOtpButton.visibility = View.VISIBLE
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        showSnackBar("Invalid Otp", Color.RED)
                    }else(
                            showSnackBar(task.exception?.message.toString(), Color.RED)
                    )
                }
            }
    }

    private fun changeActivity() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    fun showSnackBar(message: String, color: Int) {
        val rootView: View = findViewById(android.R.id.content)
        val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
        snackBar.setBackgroundTint(color)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()


    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.verifyOtpLayout.visibility == View.VISIBLE) {
            binding.verifyOtpLayout.visibility = View.GONE
            binding.mobileNumberLayout.visibility = View.VISIBLE
            binding.progressBarOtp.visibility = View.GONE
            binding.verifyOtpButton.visibility = View.GONE
            return

        } else {
            super.onBackPressed()
        }
    }

}
