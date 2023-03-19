package com.eyeque.reactiveprogramming
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface ReactiveDataRepositoryInterface {
    //flow for corutine
    suspend fun workOnJobOneFlow(): Flow<ResponseData>

    suspend fun workOnJobTwoFlow(): Flow<ResponseData>

    //observable  for rxjava
    fun workOnJob1Observable(): Observable<ResponseData>

    fun workOnJob2Observable(): Observable<ResponseData>
}
