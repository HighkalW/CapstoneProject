package com.example.capstoneproject.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.capstoneproject.remote.response.LoginResponse
import com.example.capstoneproject.remote.response.RegisterResponse
import com.example.capstoneproject.remote.retrofit.ApiService
import com.example.capstoneproject.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException


class UserRepo private constructor(
    private val dataStore: DataStore<Preferences>,
    private val apiService: ApiService
){

    fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result =apiService.register(name,email, password)
            emit(Result.Success(result))
        }catch (throwable: HttpException)
        {
            try {
                throwable.response()?.errorBody()?.source()?.let {
                    emit(Result.Error(it.toString()))
                }
            }catch (exception: Exception)
            {
                emit(Result.Error(exception.message.toString()))
            }
        }

    }

    fun login(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Result.Success(result))
        }catch (throwable: HttpException){
            try {
                throwable.response()?.errorBody()?.source()?.let {
                    emit(Result.Error(it.toString()))
                }
            } catch (exception: Exception) {
                emit(Result.Error(exception.message.toString()))
            }
        }
    }

    fun isLogin() : Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun setToken(token: String, isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[STATE_KEY] = isLogin
        }
    }

    fun getToken() : Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepo? = null

        private val TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(
            dataStore: DataStore<Preferences>,
            apiService: ApiService
        ): UserRepo {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepo(dataStore, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}