package com.example.capstoneproject.domain

import android.annotation.SuppressLint
import com.example.capstoneproject.remote.request.CommentRequest
import com.example.capstoneproject.remote.response.ApiResponse
import com.example.capstoneproject.remote.response.CommentResponse
import com.example.capstoneproject.remote.response.RemoteDataSource
import com.example.capstoneproject.util.ClassMapper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RepositoryUseCase(private val remote: RemoteDataSource): RepositoryInterface {
    private val result = PublishSubject.create<CommentResponse>()
    private val mCompositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    override fun getResult(content: Request): Flowable<Comments> {
        val requestMessage = CommentRequest(
            content.comment
        )
        remote.getResult(requestMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .doOnComplete {
                mCompositeDisposable.dispose()
            }
            .subscribe{
                when(it){
                    is ApiResponse.Success -> result.onNext(it.data)
                    else -> result.onNext(
                        CommentResponse(
                            "notfound404"
                        )
                    )
                }
            }
        val value = result.toFlowable(BackpressureStrategy.BUFFER)
        return value.map { ClassMapper.mapResponseToDomain(it) }
    }
}