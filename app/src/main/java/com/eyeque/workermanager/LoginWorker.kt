package com.eyeque.workermanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.eyeque.enterprisedatamodule.model.request.Credential
import com.eyeque.enterprisedatamodule.repository.EnterpriseDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch

@HiltWorker
class LoginWorker @AssistedInject constructor(@Assisted context: Context, @Assisted workerParameters: WorkerParameters, private val enterpriseDataRepository: EnterpriseDataRepository): CoroutineWorker(context, workerParameters) {

    companion object{
        const val LOGIN_WORK_SUCCESS_KEY = "success"
        const val LOGIN_WORK_SUCCESS_FAILED = "failed"
    }

    override suspend fun doWork(): Result {
        var resultData = Data.Builder().build()
        enterpriseDataRepository.loginFlow(Credential("long.chen@eyeque.com", "123456")).catch { e->
            e.printStackTrace()
            resultData = Data.Builder().putString(LOGIN_WORK_SUCCESS_FAILED, e.message).build()
        }.collect{
            resultData = Data.Builder().putStringArray(LOGIN_WORK_SUCCESS_KEY, it.roles.toTypedArray()).build()
        }
        return Result.success(resultData)
    }

}