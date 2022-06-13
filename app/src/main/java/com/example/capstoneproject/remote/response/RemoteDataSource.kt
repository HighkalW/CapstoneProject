package com.example.capstoneproject.remote.response

import android.annotation.SuppressLint
import android.util.Log
import com.example.capstoneproject.domain.RemoteDataInterface
import com.example.capstoneproject.remote.request.CommentRequest
import com.example.capstoneproject.remote.request.RemoteInterface
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RemoteDataSource(private val retrofit: RemoteInterface): RemoteDataInterface {
    @SuppressLint("CheckResult")
    override fun getResult(content: CommentRequest): Flowable<ApiResponse<CommentResponse>> {
        val returnValue = PublishSubject.create<ApiResponse<CommentResponse>>()
        val call = retrofit.fetchComment(content)
        call
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe ({ response ->
                returnValue.onNext(if (response.code.isNotEmpty()) ApiResponse.Success(response) else ApiResponse.Empty)
            }, { error ->
                returnValue.onNext(ApiResponse.Error(error.message.toString()))
                Log.e("RemoteDataSource", error.toString())
            })
        return returnValue.toFlowable(BackpressureStrategy.BUFFER)
    }

    /*@SuppressLint("CheckResult")
    override fun getResult(content: RequestEntity): Flowable<ApiResponse<NewsResponse>> {
        val returnValue = PublishSubject.create<ApiResponse<NewsResponse>>()
        val call = retrofit.testFetchNews(content.message)
        call
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe ({ response ->
                Log.d("OwO", "getResult: $response")
                returnValue.onNext(if (response.judul.isNotEmpty()) ApiResponse.Success(response) else ApiResponse.Empty)
            }, { error ->
                Log.d("OwO", "getResult: ${error.message}")
                returnValue.onNext(ApiResponse.Error(error.message.toString()))
                Log.e("RemoteDataSource", error.toString())
            })
        return returnValue.toFlowable(BackpressureStrategy.BUFFER)
    }*/
}