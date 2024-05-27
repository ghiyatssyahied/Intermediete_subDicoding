package com.dicoding.picodiploma.loginwithanimation.data.retrofit.api

import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.ListStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @FormUrlEncoded
    @POST("register")
    suspend fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getStories(): ListStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun upload(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : UploadResponse


}