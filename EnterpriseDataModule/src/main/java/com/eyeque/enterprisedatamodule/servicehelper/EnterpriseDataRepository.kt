package com.eyeque.enterprisedatamodule.servicehelper

import com.eyeque.enterprisedatamodule.model.request.Credential
import com.eyeque.enterprisedatamodule.model.response.LoginResponse
import com.eyeque.enterprisedatamodule.model.response.PatientResponse
import com.eyeque.enterprisedatamodule.model.response.UserInfoResponse
import com.eyeque.enterprisedatamodule.model.response.encounter.EncounterShortInfo
import com.eyeque.enterprisedatamodule.model.response.encounter.PatientHistoryResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface EnterpriseDataRepository {
    suspend fun login(credential: Credential): Response<LoginResponse>
    suspend fun loginFlow(credential: Credential): Flow<LoginResponse>
    suspend fun getUserInfoFlow(bearerToken: String): Flow<UserInfoResponse>
    suspend fun getPatientListFlow(bearerToken: String): Flow<List<PatientResponse>>
    suspend fun getPatientHistoryFlow(bearerToken: String, encounterId: Long): Flow<PatientHistoryResponse>
    suspend fun getPatientRxOrderListFlow(bearerToken: String, patiendId: Long): Flow<List<EncounterShortInfo>>
}