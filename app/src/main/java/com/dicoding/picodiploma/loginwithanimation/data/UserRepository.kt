// UserRepository.kt
package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.di.Result
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.ListStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.UploadResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private var apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun userLogin(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.userLogin(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun userRegister(name: String, email: String, password: String): Flow<Result<RegisterResponse>> = flow {
        try {
            val response = apiService.userRegister(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateApiService(apiService: ApiService){
        this.apiService = apiService
    }

    suspend fun getStories(): Flow<Result<ListStoryResponse>> = flow {
        try {
            val response = apiService.getStories()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailStories(id: String) : Flow<Result<DetailStoryResponse>> = flow{
        try {
            val response = apiService.getDetailStories(id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun uploadImg(imageFile: File, description: String) : Flow<Result<UploadResponse>> = flow {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImg = imageFile.asRequestBody("image/jpg".toMediaType())
        val bodyMultipart = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImg
        )
        try {
            val responseSuccess = apiService.upload(bodyMultipart, requestBody)
            emit(Result.Success(responseSuccess))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val responseError = Gson().fromJson(errorBody, UploadResponse::class.java)
            emit(Result.Error(responseError.message))
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userPreference: UserPreference, apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference,apiService)
            }.also { instance = it }
    }
}
