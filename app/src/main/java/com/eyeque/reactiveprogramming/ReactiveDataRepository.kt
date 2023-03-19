package com.eyeque.reactiveprogramming

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ReactiveDataRepository constructor(private val longJobs: LongJobs): ReactiveDataRepositoryInterface {
    override suspend fun workOnJobOneFlow(): Flow<ResponseData> {
        return flow {
            val responseData = longJobs.workOnJobOne()
            emit(responseData)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun workOnJobTwoFlow(): Flow<ResponseData> {
        return flow {
            val responseData = longJobs.workOnJobTwo()
            emit(responseData)
        }.flowOn(Dispatchers.IO)
    }

    override fun workOnJob1Observable(): Observable<ResponseData> {
        return longJobs.workOnJob1Observable()
    }

    override fun workOnJob2Observable(): Observable<ResponseData> {
        return longJobs.workOnJob2Observable()
    }
}