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
import android.widget.AdapterView
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)
    private var homeViewModel: HomeViewModel? = null

    private var graphActivityId = 1
    val entries: MutableList<BarEntry> = ArrayList()
    val colorList: MutableList<Int> = mutableListOf(
        Color.parseColor("#ACDDDE"),
//        Color.parseColor("#CAF1DE"),
//        Color.parseColor("#E1F8DC"),
//        Color.parseColor("#FEF8DD"),
//        Color.parseColor("#FFE7C7"),
//        Color.parseColor("#F7D8BA")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel!!.responseError.observe(this) {
            showSnackBar(it)
        }
        binding = FragmentHomeBinding.inflate(layoutInflater)
        homeViewModel!!.userName.set(sharedPref.getString(Constants.Admin_Username_Key, ""))

        binding.viewModel = homeViewModel
        Glide.with(this).load(sharedPref.getString(Constants.Admin_Image_Key, ""))
            .into(binding.adminProfileView)


        binding.activitySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    graphActivityId = position + 1
                    homeViewModel?.getGraphData(graphActivityId)
                    Log.d("sachin", "onItemSelected")
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {

                }
            }

        homeViewModel?.graphResponse?.observe(this) {
            entries.clear()
            if (it != null) {
                val labels = mutableListOf<String>()
                for ((index, item) in it.withIndex()) {
                    entries.add(BarEntry(index.toFloat(), item.unit.toFloat()))
                    labels.add(item.date)
                }
                val dataSet = BarDataSet(entries, "")
                dataSet.colors = colorList
                val barData = BarData(dataSet)
                binding.barChartView.data = barData
                binding.barChartView.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                val barWidth = 0.9f
                barData.barWidth = barWidth
                binding.barChartView.setVisibleXRangeMaximum(7f)
                binding.barChartView.invalidate()
                binding.barChartView.moveViewToX(0f)
            }else{
                showSnackBar("No Graph Data Found For Activity")
            }


        }

        val barChart = binding.barChartView
        barChart.isScaleYEnabled =false
        barChart.setScaleEnabled(false)
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        val calendar: Calendar = Calendar.getInstance()
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val currentMonthName: String = monthFormat.format(calendar.time)
        barChart.description.text = currentMonthName
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