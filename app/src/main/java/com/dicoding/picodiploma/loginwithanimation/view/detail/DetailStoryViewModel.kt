package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository

class DetailStoryViewModel (private val repository: UserRepository) : ViewModel(){
    suspend fun getDetailStories(id: String) = repository.getDetailStories(id)
}