package com.example.capstoneproject.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.data.repo.LoginDataSource
import com.example.capstoneproject.data.repo.LoginRepository
import com.example.capstoneproject.data.repo.UserRepo
import com.example.capstoneproject.di.UserInject

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory private constructor(private val userRepo: UserRepo): ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null

        fun getInstance(context: Context): LoginViewModelFactory =
            instance
                ?: synchronized(this)
                {
                    instance ?: LoginViewModelFactory(UserInject.provideRepository(context))
                }.also { instance = it }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}