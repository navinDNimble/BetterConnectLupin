package com.nimble.lupin.admin.views.navigation.user

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.adapters.UsersAdapter
import com.nimble.lupin.admin.databinding.FragmentUserListBinding
import com.nimble.lupin.admin.interfaces.OnUserSelected
import com.nimble.lupin.admin.models.UserModel
import com.nimble.lupin.admin.utils.Constants
import com.nimble.lupin.admin.utils.PaginationScrollListener
import com.nimble.lupin.admin.views.home.MainActivity

class UserListFragment : Fragment()  ,OnUserSelected {

    private var userListViewModel: UserListViewModel? = null

    private lateinit var paginationScrollListener: PaginationScrollListener

    private lateinit var adapter: UsersAdapter

    private lateinit var userList: MutableList<UserModel>

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private var searchDelayMillis = 500L  // 500 milliseconds
    private val textListener =  object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(searchText: String?): Boolean {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                userListViewModel!!.page=0
                userListViewModel!!.searchKey=searchText.toString()
                userListViewModel!!.getUsersList()
            }, searchDelayMillis)

            return true
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userListViewModel = ViewModelProvider(this)[UserListViewModel::class.java]
        userListViewModel!!.responseError.observe(this) {
            showSnackBar(it)
        }
        userListViewModel!!.page = 0
        userListViewModel!!.isLastPage =false
        userListViewModel!!.searchKey =""

        userList = mutableListOf()
        adapter = UsersAdapter(userList,this)
        userListViewModel!!.taskListResponse.observe(this, Observer {
            if (userListViewModel!!.page==0){
                userList.clear()
            }
            Log.d("sachinUserList",it.toString())
            userList.addAll(it)
            adapter.updateList(userList)
            adapter.notifyDataSetChanged()
        })
        userListViewModel!!.loadingProgressBar.observe(this,
            Observer {
            if (it){
                binding.progressBarUserList.visibility = View.VISIBLE
            }else{
                binding.progressBarUserList.visibility = View.GONE
            }
        })

        userListViewModel!!.getUsersList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createUserButtonId.setOnClickListener {
            val action = UserListFragmentDirections.userListFragmentToCreateUserFragment()
            findNavController().navigate(action)
        }
        binding.backButtonIdUserList.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        binding.recyclerViewAllUserList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAllUserList.adapter = adapter
        paginationScrollListener = object : PaginationScrollListener(binding.recyclerViewAllUserList.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return userListViewModel!!.isLastPage
            }

            override fun isLoading(): Boolean {
                return userListViewModel!!.loadingProgressBar.value!!
            }

            override fun loadMoreItems() {
                if (userListViewModel!!.isLastPage.not()) {
                    userListViewModel!!.page += Constants.PAGE_SIZE
                    userListViewModel!!.getUsersList()
                }
            }
        }
        paginationScrollListener.let { progressPaginationScrollListener ->
            binding.recyclerViewAllUserList.addOnScrollListener(
                progressPaginationScrollListener
            )
        }
    }


    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
        binding.searchViewUserList.setOnQueryTextListener(textListener)
        if (Constants.isChanged){
            userListViewModel?.page = 0
            userListViewModel!!.getUsersList()
            Constants.isChanged =false
        }
    }

    override fun onStop() {
        super.onStop()
        binding.searchViewUserList.setOnQueryTextListener(null)
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
    override fun onUserSelected(userModel: UserModel) {
        val action = UserListFragmentDirections.userListFragmentToUserTaskListFragment(userModel)
        findNavController().navigate(action)
    }
}