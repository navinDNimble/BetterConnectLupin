package com.nimble.lupin.user.views.navigation.logout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nimble.lupin.user.R
import com.nimble.lupin.user.views.home.MainActivity

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
            //TODO:CHANGE ACTIVITY TO LOGIN ACTIVITY
            startActivity(Intent(requireContext(), MainActivity::class.java))


        }
    }
}