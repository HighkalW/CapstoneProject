package com.example.capstoneproject.domain

import com.example.capstoneproject.remote.request.CommentRequest
import com.example.capstoneproject.remote.response.ApiResponse
import com.example.capstoneproject.remote.response.CommentResponse
import io.reactivex.Flowable

interface RemoteDataInterface {
    fun getResult(content: CommentRequest): Flowable<ApiResponse<CommentResponse>>
}