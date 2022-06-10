package com.example.capstoneproject.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
            is Loading -> "Loaded[showLoading=true]"
        }
    }
}