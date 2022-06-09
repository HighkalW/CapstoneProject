package com.example.capstoneproject.remote.response

import com.google.gson.annotations.SerializedName

data class StoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListItemStory>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
