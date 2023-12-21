package com.nimble.lupin.admin.views.home.schedule

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.adapters.ImagesAdapter
import com.nimble.lupin.admin.adapters.TaskDetailsAdapter
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.databinding.FragmentImageDetailBinding
import com.nimble.lupin.admin.databinding.FragmentScheduleUpdatesBinding
import com.nimble.lupin.admin.interfaces.OnClickSeePhoto
import com.nimble.lupin.admin.models.TaskModel
import com.nimble.lupin.admin.models.TaskUpdatesModel
import com.nimble.lupin.admin.models.UserTaskModel
import com.nimble.lupin.admin.views.home.MainActivity
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScheduleUpdatesFragment : Fragment(), OnClickSeePhoto {

    private var _binding: FragmentScheduleUpdatesBinding? = null
    private val binding get() = _binding!!
    private lateinit var tasksUpdateList: MutableList<TaskUpdatesModel>
    private lateinit var taskUserAdapter: TaskDetailsAdapter
    private var userTaskModel: UserTaskModel? = null
    private var task: TaskModel? = null

    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userTaskModel = arguments?.getParcelable("UserTaskModel")
        task = arguments?.getParcelable("TaskDetail")
        tasksUpdateList = mutableListOf()
        taskUserAdapter = TaskDetailsAdapter(tasksUpdateList, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleUpdatesBinding.inflate(inflater, container, false)

        binding.includedLayout.textViewAssignTaskTaskTitleIn.text = task?.taskId.toString()+"  " +task?.taskName
        binding.includedLayout.textViewAssignTaskStartDateIn.text =
            getString(R.string.date_combine_string, task?.startDate, task?.endDate)
        binding.includedLayout.textViewActivityNameIn.text =
            getString(R.string.activity_combine_String, task?.activityName, task?.subActivityName)
        binding.includedLayout.units.visibility = View.GONE

        binding.backButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }


        binding.taskUpdatesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.taskUpdatesRecyclerView.adapter = taskUserAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTaskUpdates()
    }

    private fun getTaskUpdates() {
        binding.scheduleUpdatesProgressBar.visibility = View.VISIBLE
        apiService.getUserTaskDetails(userTaskModel!!.userTaskId)
            .enqueue(object : Callback<ResponseHandler<List<TaskUpdatesModel>>> {
                override fun onResponse(
                    call: Call<ResponseHandler<List<TaskUpdatesModel>>>,
                    response: Response<ResponseHandler<List<TaskUpdatesModel>>>
                ) {
                    val result = response.body()
                    if (result?.code!! == 200) {
                        val resultList = result.response

                        taskUserAdapter.updateList(resultList)

                    } else if (result.code == 404) {

                        binding.scheduleUpdatesResultTextView.text = result.message
                    } else if (result.code == 500) {
                        showSnackBar(result.message)
                    }
                    binding.scheduleUpdatesProgressBar.visibility = View.GONE
                }

                override fun onFailure(
                    call: Call<ResponseHandler<List<TaskUpdatesModel>>>,
                    t: Throwable
                ) {
                    binding.scheduleUpdatesProgressBar.visibility = View.GONE
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

    override fun onClickSeePhoto(taskUpdateId: Int) {

        ImageDialog(taskUpdateId, requireContext()).show()

    }

    class ImageDialog(private val taskUpdateId: Int, bottomContext: Context) :
        BottomSheetDialog(bottomContext) {

        lateinit var binding: FragmentImageDetailBinding
        private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = FragmentImageDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)

            apiService.getPhotosUrl(taskUpdateId)
                .enqueue(object : Callback<ResponseHandler<List<String>>> {
                    override fun onResponse(
                        call: Call<ResponseHandler<List<String>>>,
                        response: Response<ResponseHandler<List<String>>>
                    ) {
                        val result = response.body()
                        if (result?.code == 200) {
                            val list = result.response
                            val imagesAdapter = ImagesAdapter(list)
                            binding.imageRecyclerView.layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            binding.imageRecyclerView.adapter = imagesAdapter
                            binding.progressBarImages.visibility = View.GONE
                        } else if (result?.code == 400) {
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            binding.progressBarImages.visibility = View.GONE
                        }

                    }

                    override fun onFailure(
                        call: Call<ResponseHandler<List<String>>>,
                        t: Throwable
                    ) {
                        binding.progressBarImages.visibility = View.GONE
                    }

                })

        }

    }
}

