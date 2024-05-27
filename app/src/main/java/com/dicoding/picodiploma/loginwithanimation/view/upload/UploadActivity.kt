package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.di.Result
import com.dicoding.picodiploma.loginwithanimation.view.upload.UploadUtils.Companion.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityUploadBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import kotlinx.coroutines.launch


class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    private var imageUri: Uri? = null

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { gallery() }
        binding.cameraButton.setOnClickListener { camera() }
        binding.uploadButton.setOnClickListener { uploadImg() }
    }

    private fun gallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun camera() {
        imageUri = UploadUtils.getImageUri(this)
        launchCamera.launch(imageUri)
    }

    private fun uploadImg() {
        imageUri?.let { uri ->
            val fileImage = UploadUtils.uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImg: ${fileImage.path}")
            val description = binding.edAddDescription.text.toString()

            lifecycleScope.launch {
                viewModel.uploadImg(fileImage, description).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            loading(true)
                        }

                        is Result.Success -> {
                            toast(result.data.message)
                            loading(false)

                            val intent = Intent(this@UploadActivity,MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)

                        }

                        is Result.Error -> {
                            toast(result.error)
                            loading(false)
                        }
                    }
                }
            }
        } ?: toast(getString(R.string.NoImg))
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            prevImg()
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private val launchCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            prevImg()
        }
    }

    private fun prevImg() {
        imageUri?.let {
            Log.d("Image URI", "showImg: $it")
            binding.previewImage.setImageURI(it)
        }
    }

    private fun loading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
