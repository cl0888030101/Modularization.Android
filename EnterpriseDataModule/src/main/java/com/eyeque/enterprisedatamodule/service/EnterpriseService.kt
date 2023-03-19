package com.eyeque.enterprisedatamodule.service

import com.eyeque.enterprisedatamodule.model.request.Credential
import com.eyeque.enterprisedatamodule.model.response.LoginResponse
import com.eyeque.enterprisedatamodule.model.response.PatientResponse
import com.eyeque.enterprisedatamodule.model.response.UserInfoResponse
import com.eyeque.enterprisedatamodule.model.response.encounter.EncounterShortInfo
import com.eyeque.enterprisedatamodule.model.response.encounter.PatientHistoryResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface EnterpriseService {
    @POST("/enterprise_backend/api/login")
    suspend fun login(@Body credential: Credential): Response<LoginResponse>

    @POST("/enterprise_backend/api/login")
    suspend fun login2(@Body credential: Credential): LoginResponse

    @GET("/enterprise_backend/api/authenticate")
    suspend fun getUserInfo(@Header("Authorization") bearerToken: String): UserInfoResponse

    @GET("/enterprise_backend/api/patients")
    suspend fun getPatientList(@Header("Authorization") bearerToken: String): List<PatientResponse>

    @GET("/enterprise_backend/api/patients/encounts/{encounterId}/history")
    suspend fun getPatientHistory(@Header("Authorization") bearerToken: String, @Path("encounterId")encounterId: Long): PatientHistoryResponse

    @GET("/enterprise_backend/api/patients/{patientId}/encounters")
    suspend fun getPatientRxOrderList(@Header("Authorization") bearerToken: String, @Path("patientId") patientId: Long): List<EncounterShortInfo>

    @GET("/somewhere/api/musicfile/{filename}")
    @Streaming
    suspend fun downloadMusicFile(@Path("fileName") fileName: String):ResponseBody

}