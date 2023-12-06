package com.nimble.lupin.user.views.home.task.taskDetail

import android.app.Dialog
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.user.adapters.TaskDetailsAdapter
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nimble.lupin.user.R
import com.nimble.lupin.user.api.ApiService
import com.nimble.lupin.user.api.ResponseHandler
import com.nimble.lupin.user.databinding.FragmentTaskDetailBinding
import com.nimble.lupin.user.interfaces.OnClickSeePhoto
import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.models.TaskUpdatesModel
import com.nimble.lupin.user.utils.Constants
import com.nimble.lupin.user.views.home.MainActivity
import com.nimble.lupin.user.views.home.task.TaskFragment
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaskDetailFragment : Fragment() , OnClickSeePhoto {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private var task: TaskModel? = null
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =  FragmentTaskDetailBinding.inflate(layoutInflater)
        task = arguments?.getParcelable("TaskDetail")

        binding.includedLayout.textViewAssignTaskTaskTitleIn.text = task?.task?.taskName
        binding.includedLayout.textViewAssignTaskStartDateIn.text =
            getString(R.string.date_combine_string, task?.task?.startDate, task?.task?.endDate)
        binding.includedLayout.textViewActivityNameIn.text = getString(
            R.string.activity_combine_String,
            task?.task?.activityName,
            task?.task?.subActivityName
        )
        binding.includedLayout.units.text = getString(
            R.string.units_combine_String,
            task?.userTask?.completedUnit.toString(),
            task?.userTask?.totalUnits.toString()
        )
        binding.backButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        setUpdateTaskButton()
        binding.updateTaskButton.setOnClickListener {
            if (task?.userTask!!.completedUnit<task?.userTask!!.totalUnits){
                val action = TaskDetailFragmentDirections.taskDetailFragmentToTaskUpdateFragment(task!!)
                findNavController().navigate(action)
            }else{
                showSnackBar("Task Is Already Completed")
            }
        }
        getTaskUpdates()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    private fun setUpdateTaskButton() {
        if (task?.userTask!!.completedUnit==task?.userTask!!.totalUnits){
            binding.updateTaskButton.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
    private fun getTaskUpdates() {
        binding.taskDetailProgressBar.visibility = View.VISIBLE
        apiService.getUserTaskDetails(task?.userTask!!.userTaskId)
            .enqueue(object : Callback<ResponseHandler<List<TaskUpdatesModel>>> {
                override fun onResponse(
                    call: Call<ResponseHandler<List<TaskUpdatesModel>>>,
                    response: Response<ResponseHandler<List<TaskUpdatesModel>>>
                ) {
                    val result = response.body()
                    if (result?.code!! == 200) {
                        val resultList = result.response
                        val taskDetailsAdapter = TaskDetailsAdapter(resultList , this@TaskDetailFragment)
                        binding.taskDetailRecyclerView.layoutManager = LinearLayoutManager(context)
                        binding.taskDetailRecyclerView.adapter = taskDetailsAdapter

                        val size = resultList.size
                        if (size < task?.userTask!!.totalUnits){
                            binding.updateTaskButton.isEnabled = true
                        }
                        if (size != task!!.userTask?.completedUnit ){
                            task!!.userTask?.completedUnit = size
                            Constants.changedSize = size
                            binding.includedLayout.units.text = getString(R.string.units_combine_String,size.toString(),task?.userTask!!.totalUnits.toString())
                        }
                        setUpdateTaskButton()
                        binding.updateTaskButton.isEnabled = true
                    } else if (result.code == 404) {
                        binding.updateTaskButton.isEnabled = true
                        binding.taskDetailTextView.text = result.message
                    } else if (result.code == 500){
                        showSnackBar(result.message)
                        binding.updateTaskButton.isEnabled =false
                    }
                    binding.taskDetailProgressBar.visibility = View.GONE
                }

                override fun onFailure(
                    call: Call<ResponseHandler<List<TaskUpdatesModel>>>,
                    t: Throwable
                ) {
                    binding.taskDetailProgressBar.visibility = View.GONE
                    binding.updateTaskButton.isEnabled =false
                    showSnackBar(t.message.toString())
                }

            }
            )



    }
    fun showSnackBar(message: String) {

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
    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("sachin","onDestroy() task Deatail")
        _binding = null
    }

    override fun onClickSeePhoto(taskUpdateId: Int) {

        val action = TaskDetailFragmentDirections.taskDetailFragmentToImageDetailFragment(taskUpdateId)
        findNavController().navigate(action)

    }
}