package com.example.capstoneproject.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.data.repo.StoryRepo
import com.example.capstoneproject.data.repo.UserRepo
import com.example.capstoneproject.di.StoryInject
import com.example.capstoneproject.di.UserInject
import com.example.capstoneproject.ui.main.MainViewModel
import com.example.capstoneproject.ui.story.StoryViewModel

class StoryVMF private constructor(private val userRepo: UserRepo, private val storyRepo: StoryRepo) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepo, storyRepo) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(storyRepo) as T
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryVMF? = null
        fun getInstance(context: Context): StoryVMF =
            instance ?: synchronized(this) {
                instance ?: StoryVMF(UserInject.provideRepository(context), StoryInject.provideRepository(context))
            }.also { instance = it }
    }
}