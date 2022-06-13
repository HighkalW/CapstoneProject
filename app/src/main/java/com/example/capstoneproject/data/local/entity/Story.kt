package com.example.capstoneproject.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class Story(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    @ColumnInfo(name = "photo_url") val photoUrl: String,
    @ColumnInfo(name="created_at") val createdAt: String,
): Parcelable