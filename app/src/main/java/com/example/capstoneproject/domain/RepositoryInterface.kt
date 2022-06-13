package com.example.capstoneproject.domain

import io.reactivex.Flowable

interface RepositoryInterface {
    fun getResult(content: Request): Flowable<Comments>
}