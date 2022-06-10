package com.example.capstoneproject.ui.signup

import androidx.lifecycle.ViewModel
import com.example.capstoneproject.data.repo.UserRepo


class SignupViewModel(private val repo: UserRepo) : ViewModel() {

    fun register(name: String, email: String, password: String) = repo.register(name, email, password)
}