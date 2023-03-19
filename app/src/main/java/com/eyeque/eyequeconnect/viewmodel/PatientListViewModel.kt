package com.eyeque.eyequeconnect.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.eyeque.enterprisedatamodule.APIResult
import com.eyeque.enterprisedatamodule.model.response.PatientResponse
import com.eyeque.enterprisedatamodule.model.response.UserInfoResponse
import com.eyeque.enterprisedatamodule.model.response.encounter.EncounterShortInfo
import com.eyeque.enterprisedatamodule.repository.EnterpriseDataRepository
import com.eyeque.eyequeconnect.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientListViewModel @Inject constructor(private val enterpriseDataRepository: EnterpriseDataRepository) : ViewModel() {
    private val _userInfoFlow = MutableStateFlow<APIResult<UserInfoResponse>>(APIResult.Idle(true))
    val userInfoFlow: StateFlow<APIResult<UserInfoResponse>> = _userInfoFlow

    private val _userInfoFlow2 = MutableStateFlow<APIResult<UserInfoResponse>>(APIResult.Idle(true))
    val userInfoFlow2: StateFlow<APIResult<UserInfoResponse>> = _userInfoFlow2

    private val _patientListFlow = MutableStateFlow<APIResult<List<PatientResponse>>>(APIResult.Idle(true))
    val patientListFlow: StateFlow<APIResult<List<PatientResponse>>> = _patientListFlow

    private val _patientEncounterListFlow = MutableStateFlow<APIResult<List<EncounterShortInfo>>>(APIResult.Idle(true))
    val patientEncounterListFlow: StateFlow<APIResult<List<EncounterShortInfo>>> = _patientEncounterListFlow

    fun getUserInfo(){
        viewModelScope.launch {
            //async: call the functions asynchronized
            val a = async {  _userInfoFlow.value = APIResult.Loading(true)
                enterpriseDataRepository.getUserInfoFlow(Constants.token ?: "").catch {e ->
                _userInfoFlow.value = APIResult.Error(e.message!!)
                _userInfoFlow.value = APIResult.Loading(false)
            }.collect{
                _userInfoFlow.value = APIResult.Success(it)
                _userInfoFlow.value = APIResult.Loading(false)
            }
            }

           val b = async {  enterpriseDataRepository.getUserInfoFlow(Constants.token ?: "").catch {e ->
            _userInfoFlow2.value = APIResult.Error(e.message!!)
            _userInfoFlow2.value = APIResult.Loading(false)
            }.collect{
                _userInfoFlow2.value = APIResult.Success(it)
                _userInfoFlow2.value = APIResult.Loading(false)
            }
           }
            a.await()
            b.await()
        }
    }

    fun getPatientList(){
        viewModelScope.launch {
            enterpriseDataRepository.getPatientListFlow(Constants.token ?: "")
                .catch { e->
                    e.printStackTrace()
                }
                .collect{
                    _patientListFlow.value = APIResult.Success(it)
                    it.forEach {
                        getPatientRxOrderList(it.id)
                    }
                }
        }
    }


    fun getPatientRxOrderList(patientId: Long){
        viewModelScope.launch {
            enterpriseDataRepository.getPatientRxOrderListFlow(Constants.token ?: "", patientId)
                .catch { e-> e.printStackTrace() }
                .collect{
                    _patientEncounterListFlow.value = APIResult.Success(it)
                    it.forEach {
                        Log.i("getPatientRxOrderList", it.toString())
                    }

                }
        }
    }

    private suspend fun countDownTimerFlow(): Flow<Int> = flow<Int> {
        var currVal = 0
        emit(currVal)
        while(currVal < 10){
            delay(1000)
            currVal += 10
            emit(currVal)
        }
        getUserInfo()
    }.flowOn(Dispatchers.IO)

    val timerFlow = MutableStateFlow(0)

    fun startCountDownTimer(){
        viewModelScope.launch {
            countDownTimerFlow().catch {e ->
                timerFlow.emit(-1)
                e.printStackTrace()
            }.collect{
                timerFlow.emit(it)
            }
        }
    }
}