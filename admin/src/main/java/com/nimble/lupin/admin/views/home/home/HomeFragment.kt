package com.nimble.lupin.admin.views.home.home

import android.content.res.Resources.Theme
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.databinding.FragmentHomeBinding
import com.nimble.lupin.admin.views.home.MainActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d("sachin","onCreateView");
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("sachin","onVIEWcREATED");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("sachin","oncreatHome");
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("sachin","ondestroyViewHome");
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()
        Log.d("sachin","onresumehome");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("sachin","ondestroyhOME");
    }
}