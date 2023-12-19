package com.nimble.lupin.pu_manager.views.navigation.user.createUser

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.pu_manager.R
import com.nimble.lupin.pu_manager.api.ApiService
import com.nimble.lupin.pu_manager.api.ResponseHandler
import com.nimble.lupin.pu_manager.databinding.FragmentCreateUserBinding
import com.nimble.lupin.pu_manager.interfaces.OnBottomSheetItemSelected
import com.nimble.lupin.pu_manager.models.BottomSheetModel
import com.nimble.lupin.pu_manager.models.UserModel
import com.nimble.lupin.pu_manager.utils.BottomSheet
import com.nimble.lupin.pu_manager.utils.Constants
import com.nimble.lupin.pu_manager.views.home.MainActivity
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
import java.util.regex.Pattern


class CreateUserFragment : Fragment(), OnBottomSheetItemSelected {

    private lateinit var binding: FragmentCreateUserBinding
    private val calendar = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var postBottomSheet: BottomSheet

    private var postId: Int = 5
    private var reportAuthorityId: Int = Constants.Admin_ID
    private val workStation = Constants.AdminWorkStation_ID
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    private  var  selectedImage  : Bitmap? = null
    private val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val settingCameraCode  = 210
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher =
            registerForActivityResult(
                RequestPermission()
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
//        val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
//            // Callback is invoked after the user selects a media item or closes the
//            // photo picker.
//            if (uri != null) {
//                Log.d("PhotoPicker", "Selected URI: $uri")
//            } else {
//                Log.d("PhotoPicker", "No media selected")
//            }
//        }
//        pickMedia.launch(PickVisualMediaRequest(ImageOnly))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateUserBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomModelList = mutableListOf(
           /* BottomSheetModel(2, "Project Manager"),
            BottomSheetModel(3, "PU Manager"),
            BottomSheetModel(4, "Project Coordinator"),*/
            BottomSheetModel(5, "Field Facilitator")
        )

        postBottomSheet = BottomSheet(bottomModelList, this, "post", false, requireContext())
        binding.reportAuthorityId.text = sharedPref.getString(Constants.Admin_Username_Key,"") +" "+ Constants.AdminWorkStation_Name
        Log.d("saching", Constants.AdminWorkStation_Name)

        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                binding.joiningDateId.text
                calendar.set(year, monthOfYear, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate: String = sdf.format(calendar.time)
                binding.joiningDateId.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        binding.backButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }


        binding.joiningDateId.setOnClickListener() {
            datePickerDialog.show()
        }
        binding.postId.setOnClickListener {
            postBottomSheet.show()
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
        binding.profileImage.setOnClickListener {
            selectorDialog.show()
        }

        binding.createUserId.setOnClickListener() {


            val firstName = binding.firstNameId.text.toString().trim()
            val lastName = binding.lastNameId.text.toString().trim()
            val mobileNumber = binding.mobileNumberId.text.toString().trim()
            val emailId = binding.emailIdId.text.toString().trim()
            val empIdNumber = binding.empIdNumberId.text.toString().trim()
            val joiningDate = binding.joiningDateId.text.toString()

            if (selectedImage ==null){
                showSnackBar("Please Select User Photo",Color.RED)
                binding.profileImage.requestFocus()
                return@setOnClickListener
            }

            if (isEmpty(firstName)) {
                binding.firstNameId.error = "FirstName Should Not Be Empty."
                binding.firstNameId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(lastName)) {
                binding.lastNameId.error = "LastName Should Not Be Empty."
                binding.lastNameId.requestFocus()
                return@setOnClickListener
            }
            if (mobileNumber.length != 10) {
                binding.mobileNumberId.error = " Enter Valid Mobile Number."
                binding.mobileNumberId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(emailId)) {
                binding.emailIdId.error = "Email Address Should Not Be Empty."
                binding.emailIdId.requestFocus()
                return@setOnClickListener
            }
            if (isValidEmail(emailId).not()) {
                binding.emailIdId.error = "Enter a Valid Email Address."
                binding.emailIdId.requestFocus()
                return@setOnClickListener
            }


            if (isEmpty(empIdNumber)) {
                binding.empIdNumberId.error = "Emp Id Number Should Not Be Empty."
                binding.empIdNumberId.requestFocus()
                return@setOnClickListener
            }
            if (reportAuthorityId == 0) {
                binding.reportAuthorityId.error = "Select ReportAuthority "
                binding.reportAuthorityId.requestFocus()
                return@setOnClickListener
            }
            if (isEmpty(joiningDate)) {
                binding.joiningDateId.error = "Joining Date  Should Not Be Empty."
                binding.joiningDateId.requestFocus()
                return@setOnClickListener
            }
            binding.createUserProgressBar.visibility = View.VISIBLE
            binding.createUserId.visibility = View.GONE

            val baos = ByteArrayOutputStream()
            var quality = 100 // initial quality
            var compressedBitmap: Bitmap? = null

            do {
                baos.reset()
                selectedImage?.compress(Bitmap.CompressFormat.JPEG, quality, baos)
                val dataSize = baos.toByteArray().size
                quality -= 10
            } while (dataSize > 200 * 1024 && quality > 0)

            val compressedData = baos.toByteArray()
            uploadProfileImage(compressedData) { imageUrl ->
                if (imageUrl != null) {
                    val userModel = UserModel(
                        0,
                        firstName,
                        lastName,
                        mobileNumber,
                        emailId,
                        workStation,null,
                        postId,
                        empIdNumber,
                        reportAuthorityId,
                        joiningDate,
                        imageUrl
                    )

                    apiService.createUser(userModel).enqueue(object : Callback<ResponseHandler<UserModel>> {
                        override fun onResponse(
                            call: Call<ResponseHandler<UserModel>>,
                            response: Response<ResponseHandler<UserModel>>
                        ) {
                            val result = response.body()
                            Log.d("sachinCreateUser", result?.message.toString())
                            if (result?.code == 200) {
                                showSnackBar(result.message, Color.GREEN)
                                Log.d("sachinCreateUser", result.message)
                                Constants.isChanged =  true
                                fragmentManager?.popBackStack()
                            } else if (result?.code == 409) {
                                Log.d("sachinCreateUser", result.message)
                                showSnackBar(result.message, Color.RED)
                            }
                            binding.createUserProgressBar.visibility = View.GONE
                            binding.createUserId.visibility = View.VISIBLE
                        }

                        override fun onFailure(call: Call<ResponseHandler<UserModel>>, t: Throwable) {
                            showSnackBar(t.message.toString(), Color.RED)
                            binding.createUserProgressBar.visibility = View.GONE
                            binding.createUserId.visibility = View.VISIBLE
                        }

                    })
                } else {
                    binding.createUserProgressBar.visibility = View.GONE
                    binding.createUserId.visibility = View.VISIBLE
                    return@uploadProfileImage
                }
            }


        }
    }

    private fun openCamera() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, 110)
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
    private fun showRationalDialog() {
        val dialog = Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_permission)
        dialog.findViewById<AppCompatTextView>(R.id.message).text =
            "CAMERA permission IS REQUIRED \n Do you Want To Allow permission"
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

    private fun uploadProfileImage(image: ByteArray, callback: (String?) -> Unit) {
        val randomName = UUID.randomUUID().toString()
        val ref = Constants.storageRef.child("Profile/$randomName")
        val uploadTask = ref.putBytes(image)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    Log.d("Sachin", "Download URL: $imageUrl")
                    callback(imageUrl)
                }
            } else {
                showSnackBar("Image upload failed: ${task.exception?.message}", Color.RED)
                Log.e("Sachin", "Image upload failed: ${task.exception?.message}")
                callback(null)
            }
        }
    }
    private fun isEmpty(userInputValue: String): Boolean {
        if (TextUtils.isEmpty(userInputValue)) {
            return true
        }
        return false
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
        return Pattern.compile(regex).matcher(email).matches()
    }

    override fun onBottomSheetItemSelected(bottomSheetItem: BottomSheetModel, type: String) {
        when (type) {
            "post" -> {
                binding.postId.text = bottomSheetItem.title
                postId = bottomSheetItem.id
                postBottomSheet.cancel()
            }


        }
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBottomView()
    }

    private fun showSnackBar(message: String, color: Int) {
        val rootView: View = requireActivity().findViewById(android.R.id.content)
        val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val params = snackBarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackBarView.layoutParams = params
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
            selectedImage = data?.extras?.get("data") as Bitmap
            Glide.with(this).load(selectedImage).into(binding.profileImage)

        } else if (requestCode ==111&&resultCode == Activity.RESULT_OK) {
            val selectedImageUri : Uri = data?.data!!

            val selectedImageBitmap: Bitmap?

            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    selectedImageUri
                )
                selectedImage = selectedImageBitmap
                Glide.with(this).load(selectedImageBitmap).into(binding.profileImage)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(),"Image Not Found", Toast.LENGTH_SHORT).show()
            }
        }

    }
}