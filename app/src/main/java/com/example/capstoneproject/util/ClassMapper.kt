package com.example.capstoneproject.util

import com.example.capstoneproject.domain.Comments
import com.example.capstoneproject.remote.response.CommentResponse

object ClassMapper{

    fun mapResponseToDomain(response: CommentResponse): Comments{
        return Comments(
            response.code,
            response.prediction
        )
    }

}
