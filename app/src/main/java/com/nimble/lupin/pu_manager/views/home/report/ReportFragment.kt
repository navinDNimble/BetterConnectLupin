package com.nimble.lupin.pu_manager.views.home.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nimble.lupin.pu_manager.adapters.ViewPagerAdapter
import com.nimble.lupin.pu_manager.databinding.FragmentReportBinding
import com.nimble.lupin.pu_manager.views.home.MainActivity

class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val fragmentList = listOf(CompletedTaskFragment(),InProgressTaskFragment(),TaskNotYetStartedFragment(),TaskReportsFragment())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewPagerAdapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = viewPagerAdapter

        val tabLayout: TabLayout? = binding.tabLayout // Assuming you have tabLayout in your layout

        TabLayoutMediator(tabLayout!!, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Completed"
                1 -> tab.text = "InProgress"
                2 -> tab.text = "Yet To Start"
                3 -> tab.text = "Reports"
            }
        }.attach()

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("sachin", "onVIEWcREATEDREPORT")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("sachin", "ondestroyViewrEPORT")

    }

    override fun onResume() {
        super.onResume()
        Log.d("sachin", "onresumerEPORT")
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("sachin", "ondestroyrEPORT")
    }
}

