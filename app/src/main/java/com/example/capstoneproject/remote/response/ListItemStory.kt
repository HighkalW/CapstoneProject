package com.example.capstoneproject.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListItemStory(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lon")
    val lon: Double?,

    @field:SerializedName("lat")
    val lat: Double?,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String

) : Parcelable