package com.nimble.lupin.user.views.navigation.logout

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.nimble.lupin.user.R
import com.nimble.lupin.user.views.home.MainActivity
import com.nimble.lupin.user.views.login.LoginActivity
import org.koin.java.KoinJavaComponent

class LogoutDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_logout_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val  cancelButton : AppCompatButton = view.findViewById(R.id.cancel_logout)
        val  continueButton : AppCompatButton = view.findViewById(R.id.continue_logout);
        cancelButton.setOnClickListener {
            dismiss()
        }
        continueButton.setOnClickListener {
            val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)
            sharedPref.edit().clear().apply()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}