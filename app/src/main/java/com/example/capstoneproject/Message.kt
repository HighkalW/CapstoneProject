package com.example.capstoneproject

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val text: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val timestamp: Long? = null
){
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}