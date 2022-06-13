package com.example.capstoneproject.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.capstoneproject.data.local.room.StoryDatabase
import com.example.capstoneproject.remote.retrofit.ApiService
import com.example.capstoneproject.data.Result
import com.example.capstoneproject.data.StoryRemoteMedia
import com.example.capstoneproject.data.local.entity.Story
import com.example.capstoneproject.remote.response.StoryResponse
import com.example.capstoneproject.remote.response.UploadResponse
import com.example.capstoneproject.util.wrapEspressoIdlingResource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class StoryRepo (private val apiService: ApiService, private val storyDatabase: StoryDatabase){

    fun uploadStory( imageMultipart: MultipartBody.Part, desc: RequestBody, title:RequestBody ): LiveData<Result<UploadResponse>> = liveData{
        emit(Result.Loading)
        try {
            val client = apiService.uploadStory(imageMultipart, desc, title )
            emit(Result.Success(client))
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }


    fun getStories(token: String): LiveData<PagingData<Story>> {
        wrapEspressoIdlingResource {
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemoteMedia(storyDatabase, apiService, token),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getAllStories()
                }
            ).liveData
        }
    }
}