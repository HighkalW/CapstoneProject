package com.example.capstoneproject.remote.retrofit

import com.example.capstoneproject.remote.response.LoginResponse
import com.example.capstoneproject.remote.response.RegisterResponse
import com.example.capstoneproject.remote.response.StoryResponse
import com.example.capstoneproject.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {


    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): StoryResponse
    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part ("image")file: MultipartBody.Part,
        @Part("desc") description: RequestBody,
        @Part("title") title: RequestBody
    ): UploadResponse
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse
}