package com.example.capstoneproject.di

import android.content.Context
import com.example.capstoneproject.data.local.room.StoryDatabase
import com.example.capstoneproject.data.repo.StoryRepo
import com.example.capstoneproject.remote.retrofit.ApiConfig


object StoryInject {
    fun provideRepository(context: Context): StoryRepo {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepo(apiService, database)
    }
}