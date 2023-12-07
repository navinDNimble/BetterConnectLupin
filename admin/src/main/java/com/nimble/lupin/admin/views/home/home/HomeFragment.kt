package com.nimble.lupin.admin.views.home.home


import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.databinding.FragmentHomeBinding
import com.nimble.lupin.admin.utils.Constants
import com.nimble.lupin.admin.views.home.MainActivity

import org.koin.java.KoinJavaComponent


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)
    private var homeViewModel: HomeViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(context,"Called HomeFragment",Toast.LENGTH_LONG).show()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel!!.responseError.observe(this) {
            showSnackBar(it)
        }
        binding = FragmentHomeBinding.inflate(layoutInflater)
        homeViewModel!!.userName.set(sharedPref.getString(Constants.Admin_Username_Key, ""))

        binding.viewModel = homeViewModel
        Glide.with(this).load(sharedPref.getString(Constants.Admin_Image_Key, ""))
            .into(binding.adminProfileView)
        val entries: MutableList<BarEntry> = ArrayList()
        entries.add(BarEntry(1f, 2f))
        entries.add(BarEntry(2f, 4f))
        entries.add(BarEntry(3f, 3f))
        entries.add(BarEntry(4f, 1f))
        entries.add(BarEntry(5f, 1f))
        entries.add(BarEntry(6f, 2f))
        entries.add(BarEntry(7f, 7f))
        entries.add(BarEntry(8f, 4f))
        entries.add(BarEntry(9f, 3f))
        entries.add(BarEntry(10f, 1f))

        val dataSet = BarDataSet(entries, "December")
        val barData = BarData(dataSet)
        binding.barChartView.data = barData
        binding.barChartView.axisLeft.isEnabled = false
        binding.barChartView.axisRight.isEnabled = false
        binding.barChartView.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.barChartView.xAxis.isEnabled = true
        binding.barChartView.xAxis.setDrawGridLines(false)
        binding.barChartView.axisLeft.setDrawGridLines(false)
        binding.barChartView.description.text = ""
        binding.barChartView.invalidate()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


    override fun onDestroyView() {
        super.onDestroyView()


    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
        homeViewModel?.getTasksStatus()
    }


    private fun showSnackBar(message: String) {
        val rootView: View = requireActivity().findViewById(android.R.id.content)
        val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val params = snackBarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackBarView.layoutParams = params
        snackBar.setBackgroundTint(Color.RED)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()

    }
}