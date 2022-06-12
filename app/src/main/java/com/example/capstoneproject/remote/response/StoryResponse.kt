package com.example.capstoneproject.remote.response

import com.google.gson.annotations.SerializedName

data class StoryResponse(

	@field:SerializedName("StoryResponse")
	val listStory: List<StoryResponseItem>
)

data class StoryResponseItem(

	@field:SerializedName("image")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("title")
	val name: String,

	@field:SerializedName("desc")
	val description: String,

	@field:SerializedName("updatedAt")
	val createAt: String
)
