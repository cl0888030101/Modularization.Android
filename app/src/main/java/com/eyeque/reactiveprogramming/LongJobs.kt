package com.eyeque.reactiveprogramming

import android.os.SystemClock
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.delay

class LongJobs {
    //suspend function for corutine
    suspend fun workOnJobOne(): ResponseData{
        delay(1000)
        return ResponseData("workOnJobOne", 1, "workOnJobOne content")
    }

    suspend fun workOnJobTwo(): ResponseData{
        delay(1000)
        return ResponseData("workOnJobTwo", 2, "workOnJobTwo content")
    }

    //observable function for rxjava
    fun workOnJob1Observable() = Observable.create<ResponseData> {
        val data = workOnJob1()
        it.onNext(data)
        it.onComplete()
    }

    fun workOnJob2Observable() = Observable.create<ResponseData> {
        val data = workOnJob2()
        it.onNext(data)
        it.onComplete()
    }

    fun worOnJob1FlowAble() = Flowable.create({emitter ->
            emitter.onNext(workOnJob2())
            emitter.onComplete()
    }, BackpressureStrategy.BUFFER)


    private fun workOnJob1(): ResponseData{
        SystemClock.sleep(1000)
        return ResponseData("workOnJob1", 1, "workOnJob1 content")
    }


    private fun workOnJob2(): ResponseData{
        SystemClock.sleep(1000)
        return ResponseData("workOnJob2", 2, "workOnJob2 content")
    }
}


data class ResponseData(val jobName: String, val jobId: Int, val jobContent: String)