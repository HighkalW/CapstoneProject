package com.example.capstoneproject.remote.request

import com.example.capstoneproject.remote.response.CommentResponse
import io.reactivex.Flowable
import retrofit2.http.*

interface RemoteInterface {
    @Headers("Content-Type:application/json")
    @POST("/predict")
    fun fetchComment(@Body request: CommentRequest): Flowable<CommentResponse>
}