package com.example.capstoneproject.ui.story

import androidx.lifecycle.ViewModel
import com.example.capstoneproject.data.repo.StoryRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepo: StoryRepo) : ViewModel() {

    fun uploadStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        desc: RequestBody,
        title: RequestBody
    ) = storyRepo.uploadStory(imageMultipart, desc, title )
}