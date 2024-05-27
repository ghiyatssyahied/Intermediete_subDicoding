package com.dicoding.picodiploma.loginwithanimation.view.upload

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import java.io.File

class UploadViewModel(private val repository: UserRepository): ViewModel() {
    fun uploadImg(file: File, description: String) = repository.uploadImg(file, description)
}