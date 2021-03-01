package com.dscvit.keats.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dscvit.keats.model.Result
import kotlinx.coroutines.Dispatchers

open class BaseRepo {
    protected fun <T> makeRequest(
        request: suspend () -> Result<T>
    ): LiveData<Result<T>> = liveData {
        emit(Result.loading())

        val response = request()

        when (response.status) {
            Result.Status.SUCCESS -> {
                emit(Result.success(response.data))
            }
            Result.Status.ERROR -> {
                emit(Result.error(data = null, message = response.message ?: "Error"))
            }
            else -> {
            }
        }
    }

    protected fun <T, A> makeRequestAndSave(
        databaseQuery: () -> LiveData<T>,
        networkCall: suspend () -> Result<A>,
        saveCallResult: suspend (A) -> Unit
    ): LiveData<Result<T>> = liveData(Dispatchers.IO) {
        emit(Result.loading())

        val source: LiveData<Result<T>> = databaseQuery.invoke().map { Result.success(it) }
        emitSource(source)

        val response = networkCall.invoke()
        when (response.status) {
            Result.Status.SUCCESS -> {
                saveCallResult(response.data!!)
            }
            Result.Status.ERROR -> {
                emit(Result.error(response.message!!))
                emitSource(source)
            }
            else -> {
            }
        }
    }
}
