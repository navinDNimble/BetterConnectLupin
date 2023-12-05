package com.nimble.lupin.user.views.home.task.taskUpdate

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.UploadTask
import com.nimble.lupin.user.R
import com.nimble.lupin.user.adapters.ImageAdapter
import com.nimble.lupin.user.api.ApiService
import com.nimble.lupin.user.api.ResponseHandler
import com.nimble.lupin.user.databinding.FragmentUpdateTaskBinding
import com.nimble.lupin.user.interfaces.OnImageUnselected
import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.models.TaskUpdatesModel
import com.nimble.lupin.user.utils.Constants
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID


class UpdateTaskFragment : Fragment() ,OnImageUnselected{

    private var _binding: FragmentUpdateTaskBinding? = null
    private val binding get() = _binding!!
    private var task: TaskModel? = null

    private val calendar = Calendar.getInstance()
    private lateinit var startDatePickerDialog: DatePickerDialog
    private val photoList = mutableListOf<Bitmap>()

    private lateinit  var  viewModel : UpdateTaskViewModel

    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    private val imageAdapter :ImageAdapter = ImageAdapter(photoList,this)

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val settingCameraCode  = 210

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UpdateTaskViewModel::class.java]
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->

                if (isGranted) {
                    openCamera()
                }
                else{
                    if(!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        showSettingDialog()
                    }
                }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateTaskBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        task = arguments?.getParcelable("TaskDetail")
        binding.taskNameTaskUpdate.text =  task?.task!!.taskName
        binding.dateTaskUpdate.text =  getString(R.string.date_combine_string,task?.task!!.startDate , task?.task!!.endDate)
        binding.activityTaskUpdate.text = getString(R.string.activity_combine_String,task?.task!!.activityName , task?.task!!.subActivityName)
        startDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                binding.dateIdSelector.text
                calendar.set(year, monthOfYear, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate: String = sdf.format(calendar.time)
                binding.dateIdSelector.text = selectedDate

            },

            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        )
        startDatePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        binding.imageViewBackButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }

      return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        visibility()

        binding.dateIdSelector.setOnClickListener {
           startDatePickerDialog.show()
        }
        val selectorDialog = BottomSheetDialog(requireContext())
        selectorDialog.setContentView(R.layout.dialog_image_selector)
        selectorDialog.findViewById<AppCompatImageView>(R.id.camera)?.setOnClickListener {
            selectorDialog.cancel()
            checkCameraPermission()

        }
        selectorDialog.findViewById<AppCompatImageView>(R.id.gallery)?.setOnClickListener {
            selectorDialog.cancel()
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(i, "Select Picture"), 111)

        }
        binding.selectImageId.setOnClickListener {
           selectorDialog.show()
        }


        binding.updateDetailsButton.setOnClickListener {
            val taskUpdateModel  = TaskUpdatesModel()
              taskUpdateModel.taskId = task?.task?.taskId
              taskUpdateModel.userId = task?.userTask?.userId
              taskUpdateModel.userTaskId = task?.userTask?.userTaskId


            if (viewModel.maleCountVisibility.get() == true){

                val maleCount  = binding.maleFarmersId.text
                if (maleCount.isNullOrEmpty()){
                    binding.maleFarmersId.error = "Enter a male Farmer count"
                    binding.maleFarmersId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.male_count = maleCount.toString().toInt()

            }
            if (viewModel.femaleCountVisibility.get() == true){
                val femaleCount  = binding.femaleFarmersId.text
                if (femaleCount.isNullOrEmpty()){
                    binding.femaleFarmersId.error = "Enter a Female Farmer count"
                    binding.femaleFarmersId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.female_count = femaleCount.toString().toInt()
            }


            if (viewModel.lgCodeVisibility.get() == true){
                val lgCode  = binding.LgCode.text
                if (lgCode.isNullOrEmpty()){
                    binding.LgCode.error = "Enter a Lg Code "
                    binding.LgCode.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.lg_code = lgCode.toString().toInt()
            }
            if (viewModel.wellVisibility.get() == true){
                val wellCount  = binding.wellsId.text
                if (wellCount.isNullOrEmpty()){
                    binding.wellsId.error = "Enter a well Count"
                    binding.wellsId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.wells_count = wellCount.toString().toInt()
            }
            if (viewModel.surveyVisibility.get() == true){
                val surveyCount  = binding.surveyId.text
                if (surveyCount.isNullOrEmpty()){
                    binding.surveyId.error = "Enter a Survey Count"
                    binding.surveyId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.survey_count = surveyCount.toString().toInt()
            }
            if (viewModel.villagesVisibility.get() == true){
                val villageCount  = binding.villageId.text
                if (villageCount.isNullOrEmpty()){
                    binding.villageId.error = "Enter a Village Count"
                    binding.villageId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.village_count = villageCount.toString().toInt()
            }

            if (viewModel.no_of_farmers.get() == true){
                val noOfFarmers  = binding.noOfFarmersId.text
                if (noOfFarmers.isNullOrEmpty()){
                    binding.noOfFarmersId.error = "Enter a No of Farmer Count"
                    binding.noOfFarmersId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.no_of_farmers = noOfFarmers.toString().toInt()
            }
            if (viewModel.finding.get() == true){
                val findings  = binding.editTextFindingsId.text
                if (findings.isNullOrEmpty()){
                    binding.editTextSubjectId.error = "Enter a findings"
                    binding.editTextSubjectId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.findings = findings.toString()
            }
            if (viewModel.subjectVisibility.get() == true){
                val subject  = binding.editTextSubjectId.text
                if (subject.isNullOrEmpty()){
                    binding.editTextSubjectId.error = "Enter a subject"
                    binding.editTextSubjectId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.subject = subject.toString()
            }
            if (viewModel.reasonForVisitVisibility.get() == true){
                val reasonForVisit  = binding.editTextReasonVisitId.text
                if (reasonForVisit.isNullOrEmpty()){
                    binding.editTextReasonVisitId.error = "Enter a reason For Visit "
                    binding.editTextReasonVisitId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.reasonForVisit = reasonForVisit.toString()
            }
            if (viewModel.farmerNameVisibility.get() == true){
                val farmerName  = binding.nameOfFarmer.text
                if (farmerName.isNullOrEmpty()){
                    binding.nameOfFarmer.error = "Enter a Farmer Name"
                    binding.nameOfFarmer.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.name_of_farmer = farmerName.toString()
            }
            if (viewModel.reasonVisibility.get() == true){
                val reason  = binding.editTextReasonId.text
                if (reason.isNullOrEmpty()){
                    binding.editTextReasonId.error = "Enter a Reason"
                    binding.editTextReasonId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.reason = reason.toString()
            }
            if (viewModel.meetingWithWhomVisibility.get() == true){
                val meetingWithWhom  = binding.editTextMeetingWhomId.text
                if (meetingWithWhom.isNullOrEmpty()){
                    binding.editTextMeetingWhomId.error = "Enter a meeting with whom"
                    binding.editTextMeetingWhomId.requestFocus()
                    return@setOnClickListener
                }
                taskUpdateModel.meeting_with_whome = meetingWithWhom.toString()
            }
            if (viewModel.photoVisibility.get() == true){
                if (photoList.size==0){
                    showSnackBar("Please Select Photo of Related Task",Color.RED)
                    return@setOnClickListener
                }
            }
            if (binding.dateIdSelector.text.isNullOrEmpty()){
                binding.dateIdSelector.error = "Please Select Date"
                binding.editTextMeetingWhomId.requestFocus()
                return@setOnClickListener
            }
            taskUpdateModel.update_date = binding.dateIdSelector.text.toString()
            viewModel.progressBarVisibility.set(true)
            binding.updateDetailsButton.visibility = View.GONE
            val photoByteList = mutableListOf<ByteArray>()
            photoList.forEach {
                photoByteList.add(reduceImageSize(it))
            }

            if (photoByteList.size == photoList.size){

                uploadImages(photoByteList ,taskUpdateModel)
            }
            else{
                showSnackBar("Unable to Compress Selected Images",Color.RED)
                viewModel.progressBarVisibility.set(false)
                binding.updateDetailsButton.visibility = View.VISIBLE
            }

        }

    }
    private fun uploadImages(
        photoByteList: MutableList<ByteArray>,
        taskUpdateModel: TaskUpdatesModel
    ) {
        val ref = Constants.storageRef.child("UploadTask")
        val uploadTaskList = mutableListOf<UploadTask>()
        val imageUrlList = mutableListOf<String>()
        photoByteList.forEach {
            val randomName = UUID.randomUUID().toString()
            val uploadTask = ref.child(randomName).putBytes(it)
            uploadTaskList.add(uploadTask)
        }

        Tasks.whenAllComplete(uploadTaskList)
            .addOnSuccessListener { taskSnapshots ->

                for ((index, uploadTask) in uploadTaskList.withIndex()) {
                    if (uploadTask.isSuccessful) {
                        val downloadUrl = (taskSnapshots[index].result as UploadTask.TaskSnapshot).metadata?.reference?.downloadUrl.toString()
                        imageUrlList.add(downloadUrl)
                    }
                }
                if (uploadTaskList.size == imageUrlList.size){
                    taskUpdateModel.photoList = imageUrlList
                    taskUpdateModel.photo = 1
                    updateTask(taskUpdateModel)
                }
            }
            .addOnFailureListener { e ->
               showSnackBar("Failed To Upload Images "+e.message.toString(),Color.RED);
            }


    }

    private fun reduceImageSize(bitmap: Bitmap)  :ByteArray {
        val baos = ByteArrayOutputStream()
        var quality = 100 // initial quality

        do {
            baos.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            val dataSize = baos.toByteArray().size
            quality -= 10
        } while (dataSize > 100 * 1024 && quality > 0)



      return baos.toByteArray()
    }

    private fun updateTask(taskUpdateModel: TaskUpdatesModel) {

        apiService.updateUserTaskDetails(taskUpdateModel).enqueue(object :
            Callback<ResponseHandler<TaskUpdatesModel>>{
            override fun onResponse(
                call: Call<ResponseHandler<TaskUpdatesModel>>,
                response: Response<ResponseHandler<TaskUpdatesModel>>
            ) {
                val result = response.body()
                if (result?.code ==200){
                    showSnackBar(result.message , Color.GREEN)
                   val action =  UpdateTaskFragmentDirections.actionTaskUpdateFragmentToTaskDetailFragment(task!!)
                    findNavController().navigate(action)
                }else if (
                    result?.code == 404
                ){
                    showSnackBar(result.message , Color.RED)

                    binding.updateDetailsButton.visibility = View.VISIBLE
                }
                viewModel.progressBarVisibility.set(false)
            }

            override fun onFailure(call: Call<ResponseHandler<TaskUpdatesModel>>, t: Throwable) {
                showSnackBar(t.message.toString() , Color.RED)
                viewModel.progressBarVisibility.set(false)
                binding.updateDetailsButton.visibility = View.VISIBLE
            }

        })
    }

    private fun visibility() {
        val subActivityId = task?.task!!.subActivityId
        when (task?.task!!.activityId) {
            1 -> {

                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)
                Log.d("sachin",task?.task!!.modeId.toString())

                when (task?.task!!.modeId) {
                    1 -> {


                        viewModel.photoVisibility.set(true)
                        viewModel.photoMessage.set("Photo")

                    }

                    2, 3 -> {
                        viewModel.photoVisibility.set(true)
                        viewModel.photoMessage.set("Upload Photo of List Of Farmer")
                    }

                    4 -> {

                        viewModel.photoVisibility.set(true)
                        viewModel.photoMessage.set("Upload Photo of List Of Farmer Called")
                    }
                }
            }

            2, 3, 4 -> {
                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)

            }

            5 -> {
                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Upload Photo of List Of Farmer Visited and Farmer Field Photo")
            }
            6->{
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Upload Photo of List Of Farmer Visited")
            }

            7 -> {
                viewModel.reasonForVisitVisibility.set(true)
                viewModel.finding.set(true)
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo")
            }

            8 -> {
                when (subActivityId) {
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 -> {

                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)

                        viewModel.photoVisibility.set(true)
                        viewModel.photoMessage.set("Photo")
                    }


                }
            }

            9 -> {
                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo")
            }

            10 -> {
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo")
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.no_of_farmers.set(true)
                        viewModel.lgCodeVisibility.set(true)
                    }

                    4 -> {
                        viewModel.wellVisibility.set(true)

                    }

                    5 -> {
                        viewModel.subjectVisibility.set(true)
                        viewModel.farmerNameVisibility.set(true)

                    }
                }

            }

            11 -> {
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo of List And Photo")
                when (subActivityId) {
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 -> {
                        viewModel.surveyVisibility.set(true)
                    }
                }
            }

            12 -> {
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo")
                when (subActivityId) {
                    1 -> {
                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                   }
                }
            }

            13 -> {
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo")
                when (subActivityId) {
                    1, 2, 3, 4, 5 -> {
                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                    }
                }
            }

            14 -> {
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo")
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                    }
                }
            }

            15 -> {
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo")
                when (subActivityId) {
                    1, 2, 3, 4, 5 -> {
                        viewModel.reasonVisibility.set(true)
                        viewModel.maleCountVisibility.set(true)
                        viewModel.femaleCountVisibility.set(true)
                    }

                    6, 7 -> {
                        viewModel.meetingWithWhomVisibility .set(true)
                    }
                }
            }

            16 -> {
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Upload Photo of FeedBack List")
                when (subActivityId) {
                    1, 2, 3 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)

                    }
                }
            }

            17 -> {

                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo")
                when (subActivityId) {
                    1, 2, 3 -> {

                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                        viewModel.villagesVisibility .set(true)
                    }
                }

            }

            18 -> {
                viewModel.photoVisibility.set(true)
                viewModel.photoMessage.set("Photo")
                viewModel.maleCountVisibility.set(true)
                viewModel.femaleCountVisibility.set(true)
            }

            19 -> {
                when (subActivityId) {
                    1, 2, 3, 4 -> {
                        viewModel.maleCountVisibility .set(true)
                        viewModel.femaleCountVisibility .set(true)
                        viewModel.photoVisibility.set(true)
                        viewModel.photoMessage.set("Photo / Photo Of Adaption Sheet")
                    }
                }
            }

            20,21,22,23,24,25,26,27,28 -> {
                viewModel.maleCountVisibility .set(true)
                viewModel.femaleCountVisibility .set(true)
                when (subActivityId) {

                    1 -> {

                    }
                    2-> {
                        viewModel.photoVisibility.set(true)
                        viewModel.photoMessage.set("Photo")
                    }
                    3-> {
                        viewModel.photoVisibility.set(true)
                        viewModel.photoMessage.set(" Photo Of Support Sheet")
                    }
                }
            }

            29 -> {
                viewModel.maleCountVisibility .set(true)
                viewModel.femaleCountVisibility .set(true)
                when (subActivityId) {
                    1-> {
                        viewModel.photoVisibility.set(true)
                        viewModel.photoMessage.set("Photo")
                    }
                    2-> {
                        viewModel.photoVisibility.set(true)
                        viewModel.photoMessage.set(" Photo Of Support Sheet")
                    }
                }
            }
        }
    }

    private fun checkCameraPermission() {

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.CAMERA) -> {
                showRationalDialog()
            }
            else -> {
                askPermission()
            }
        }

    }
    private fun askPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    private fun openCamera() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, 110)
        }

    }
    private fun showRationalDialog() {
        val dialog = Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_permission)
        dialog.findViewById<AppCompatTextView>(R.id.message).text =
            "Camera Permission Is Required \n Do you Want To Allow Permission"
        dialog.findViewById<AppCompatButton>(R.id.negativeButton).setOnClickListener {
            dialog.dismiss()
        }
        val positiveButton = dialog.findViewById<AppCompatButton>(R.id.positiveButton)
        positiveButton.text = "Allow"
        positiveButton.setOnClickListener {
            dialog.dismiss()
            askPermission()
        }
        dialog.show()
    }

    private fun showSettingDialog() {
        val dialog = Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_permission)
        dialog.findViewById<AppCompatTextView>(R.id.message).text =
            "Allow App access to your camera .\n Tap Settings > Permission ,and turn Camera On"
        dialog.findViewById<AppCompatButton>(R.id.negativeButton).setOnClickListener {
            dialog.dismiss()
        }
        val positiveButton = dialog.findViewById<AppCompatButton>(R.id.positiveButton)
        positiveButton.text = "Settings"
        positiveButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            startActivityForResult(intent, settingCameraCode)
        }
        dialog.show()
    }
    fun showSnackBar(message: String , color :Int) {

        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG);
        snackBar.setBackgroundTint(color)
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("sachin",requestCode.toString())
        if (requestCode == settingCameraCode) {
            checkCameraPermission()
        }
        else if (requestCode == 110 &&resultCode == Activity.RESULT_OK ){
           val  selectedImage = data?.extras?.get("data") as Bitmap
            photoList.add(selectedImage)
            imageAdapter.notifyItemInserted(photoList.size-1)

        }
        else if (requestCode ==111&&resultCode == Activity.RESULT_OK) {
            val selectedImageUri : Uri = data?.data!!

            val selectedImageBitmap: Bitmap?

            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    selectedImageUri
                )
                photoList.add(selectedImageBitmap)
                imageAdapter.notifyItemInserted(photoList.size-1)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(),"Image Not Found", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onImageUnselected(position: Int) {
         photoList.removeAt(position)
        imageAdapter.notifyItemRemoved(position)
    }
}