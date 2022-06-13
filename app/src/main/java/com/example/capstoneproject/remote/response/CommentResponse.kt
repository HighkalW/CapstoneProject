package com.example.capstoneproject.remote.response

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("code")
    val code: String = "",

    @SerializedName("prediction")
    val prediction: String = ""
)
