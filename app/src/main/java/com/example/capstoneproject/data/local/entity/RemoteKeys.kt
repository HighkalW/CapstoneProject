package com.example.capstoneproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val id: String,
    val nextKey: Int?,
    val prevKey: Int?
)