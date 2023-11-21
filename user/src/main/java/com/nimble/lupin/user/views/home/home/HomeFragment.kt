package com.nimble.lupin.user.views.home.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.user.R
import com.nimble.lupin.user.databinding.FragmentHomeBinding
import com.nimble.lupin.user.utils.Constants
import com.nimble.lupin.user.views.home.MainActivity
import org.koin.java.KoinJavaComponent


class HomeFragment : Fragment() {
    private lateinit var  binding :FragmentHomeBinding
    val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)

    private var homeViewModel :HomeViewModel? =null
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel!!.responseError.observe(this) {
                showSnackBar(it) }

        homeViewModel!!.userName.set(sharedPref.getString(Constants.User_Name,""))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewModel = homeViewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
        homeViewModel?.getTasksStatus()
    }
    private fun showSnackBar(message: String) {
        val snackBar = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) };
        if (snackBar != null) {
            snackBar.setBackgroundTint(Color.RED)
            snackBar.setTextColor(Color.WHITE)
            snackBar.show()
        }

    }
}

