package com.nimble.lupin.admin.views.home.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.nimble.lupin.admin.databinding.FragmentReportBinding
import com.nimble.lupin.admin.views.home.MainActivity

class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ReportViewModel::class.java)

        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("sachin","onVIEWcREATEDREPORT");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("sachin","oncreatrEPORT");
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("sachin","ondestroyViewrEPORT");
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
    }

    override fun onResume() {
        super.onResume()
        Log.d("sachin","onresumerEPORT");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("sachin","ondestroyrEPORT");
    }

}