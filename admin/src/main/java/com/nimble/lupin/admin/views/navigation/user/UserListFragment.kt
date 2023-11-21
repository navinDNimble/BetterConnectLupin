package com.nimble.lupin.admin.views.navigation.user

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nimble.lupin.admin.databinding.FragmentUserListBinding
import com.nimble.lupin.admin.views.home.MainActivity

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class UserListFragment : Fragment() {



    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        binding.createUserButtonId.setOnClickListener {
            val action = UserListFragmentDirections.userListFragmentToCreateUserFragment()
            findNavController().navigate(action)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()

    }


    override fun onDestroy() {
        super.onDestroy()

    }



    override fun onDestroyView() {
        super.onDestroyView()
    }
}