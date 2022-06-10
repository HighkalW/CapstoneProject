package com.example.capstoneproject.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.capstoneproject.data.local.entity.Story
import com.example.capstoneproject.data.repo.StoryRepo
import com.example.capstoneproject.data.repo.UserRepo
import kotlinx.coroutines.launch

class MainViewModel(private val userRepo: UserRepo, private val storyRepo: StoryRepo) : ViewModel() {

    fun getToken() : LiveData<String> {
        return userRepo.getToken().asLiveData()
    }

    fun isLogin() : LiveData<Boolean> {
        return userRepo.isLogin().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }

    fun getStories(token: String) : LiveData<PagingData<Story>> =
        storyRepo.getStories(token).cachedIn(viewModelScope)
}