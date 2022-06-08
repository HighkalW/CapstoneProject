package com.example.capstoneproject.util

import com.example.capstoneproject.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object UserUtil {
    var user: User? = null

    fun getCurrentUser() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                .get().addOnCompleteListener {
                    user = it.result?.toObject(User::class.java)

                }
        }
    }
}