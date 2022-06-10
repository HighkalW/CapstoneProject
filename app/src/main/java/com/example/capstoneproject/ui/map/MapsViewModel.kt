package com.example.capstoneproject.ui.map

import androidx.lifecycle.ViewModel
import com.example.capstoneproject.data.repo.StoryRepo

class MapsViewModel(private val storyRepo: StoryRepo) : ViewModel() {
    fun getStories(token: String) = storyRepo.getStoryLocation(token)
}