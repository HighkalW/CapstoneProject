package com.example.capstoneproject.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.data.repo.UserRepo
import com.example.capstoneproject.di.UserInject
import com.example.capstoneproject.ui.login.LoginViewModel
import com.example.capstoneproject.ui.signup.SignupViewModel


class UserVMF (private val userRepo: UserRepo) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: UserVMF? = null
        fun getInstance(context: Context): UserVMF =
            instance ?: synchronized(this) {
                instance ?: UserVMF(UserInject.provideRepository(context))
            }.also { instance = it }
    }
}