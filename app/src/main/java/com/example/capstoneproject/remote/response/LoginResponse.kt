package com.example.capstoneproject.remote.response

import com.example.capstoneproject.ui.login.LoginResult
import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
