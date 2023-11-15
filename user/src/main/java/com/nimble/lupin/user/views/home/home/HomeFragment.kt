package com.nimble.lupin.user.views.home.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nimble.lupin.user.R
import com.nimble.lupin.user.views.home.MainActivity


class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBottomView()

        Log.d("navin", "onResume called")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.pull_in_left,
            R.anim.push_out_right,
            R.anim.pull_in_left,
            R.anim.push_out_right
        )
        transaction.commit()
    }

}

