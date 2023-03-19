package com.eyeque.workermanager

import android.content.Context
import android.util.Log
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LogInWork @Inject constructor(@ApplicationContext context: Context) {

    private val workManager = WorkManager.getInstance(context)
    fun start(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<LoginWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueue(workRequest)
        Log.i("LogInWork", "start()")

        workManager.getWorkInfoByIdLiveData(workRequest.id).observeForever {
            when(it.state){
                WorkInfo.State.SUCCEEDED ->{
                    Log.i("LogInWork", "${it.outputData.getStringArray(LoginWorker.LOGIN_WORK_SUCCESS_KEY)!!.reduce { acc, s ->  acc + s}}")
                }
                else->{}
            }
        }
    }


}