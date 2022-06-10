package com.example.capstoneproject.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.capstoneproject.data.local.entity.Story

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<Story>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    fun deleteAll()
}