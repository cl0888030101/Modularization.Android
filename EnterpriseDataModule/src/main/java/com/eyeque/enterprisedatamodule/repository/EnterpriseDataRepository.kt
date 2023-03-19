package com.eyeque.enterprisedatamodule.repository

import com.eyeque.enterprisedatamodule.model.request.Credential
import com.eyeque.enterprisedatamodule.model.response.LoginResponse
import com.eyeque.enterprisedatamodule.service.EnterpriseService
import com.eyeque.enterprisedatamodule.servicehelper.EnterpriseDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class EnterpriseDataRepository @Inject constructor(private val enterpriseService: EnterpriseService):
    EnterpriseDataRepository {
    companion object{
        const val TOKEN_TYPE = "Bearer "
    }

    override suspend fun login(credential: Credential): Response<LoginResponse> = enterpriseService.login(credential)

    override suspend fun loginFlow(credential: Credential) = flow { emit(enterpriseService.login2(credential)) }.flowOn(Dispatchers.IO)

    override suspend fun getUserInfoFlow(bearerToken: String) = flow { emit(enterpriseService.getUserInfo(TOKEN_TYPE + bearerToken)) }.flowOn(Dispatchers.IO)

    override suspend fun getPatientListFlow(bearerToken: String) = flow { emit(enterpriseService.getPatientList(TOKEN_TYPE + bearerToken)) }.flowOn(Dispatchers.IO)

    override suspend fun getPatientHistoryFlow(
        bearerToken: String,
        encounterId: Long
    ) = flow {
        emit(enterpriseService.getPatientHistory(TOKEN_TYPE + bearerToken, encounterId))
    }.flowOn(Dispatchers.IO)

    override suspend fun getPatientRxOrderListFlow(
        bearerToken: String,
        patiendId: Long
    ) = flow {
        emit(enterpriseService.getPatientRxOrderList(TOKEN_TYPE + bearerToken, patiendId))
    }.flowOn(Dispatchers.IO)

    suspend fun downloadMusicFile(fileName: String) = flow{
        val responseBody = enterpriseService.downloadMusicFile(fileName)
        //should move this part into helper file
        val inputStream = responseBody.byteStream()
        val file = File.createTempFile("music", "mp3")
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                val buffer = ByteArray(8 * 1024)
                var bytesRead = input.read(buffer)
                while (bytesRead != -1) {
                    output.write(buffer, 0, bytesRead)
                    bytesRead = input.read(buffer)
                }
            }
        }
        emit(file)
    }.flowOn(Dispatchers.IO)


}