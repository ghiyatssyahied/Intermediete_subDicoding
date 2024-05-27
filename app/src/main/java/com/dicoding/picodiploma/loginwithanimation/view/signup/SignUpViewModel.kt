
package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.flow.Flow
import com.dicoding.picodiploma.loginwithanimation.di.Result

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    fun userRegister(name: String, email: String, password: String): Flow<Result<RegisterResponse>> {
        return repository.userRegister(name, email, password)
    }
}
