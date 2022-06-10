package com.example.capstoneproject.ui.login

import android.util.Patterns
import androidx.lifecycle.*


import com.example.capstoneproject.R
import com.example.capstoneproject.data.repo.UserRepo
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepo) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(email: String, password: String) = repo.login(email, password)

    fun setToken(token: String, isLogin: Boolean){
        viewModelScope.launch {
            repo.setToken(token, isLogin)
        }
    }
    fun getToken() : LiveData<String> {
        return repo.getToken().asLiveData()
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}