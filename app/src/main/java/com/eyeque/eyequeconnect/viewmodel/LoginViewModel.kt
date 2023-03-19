package com.eyeque.eyequeconnect.viewmodel

import androidx.lifecycle.*
import com.eyeque.enterprisedatamodule.APIResult
import com.eyeque.enterprisedatamodule.model.request.Credential
import com.eyeque.enterprisedatamodule.model.response.LoginResponse
import com.eyeque.enterprisedatamodule.repository.EnterpriseDataRepository
import com.eyeque.eyequeconnect.utils.Constants
import com.eyeque.reactiveprogramming.ReactiveDataRepository
import com.eyeque.reactiveprogramming.ResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(private val enterpriseDataRepository: EnterpriseDataRepository, private val reactiveDataRepository: ReactiveDataRepository) : ViewModel() {
    companion object{
        const val TAG = "LoginViewModel"
    }

    private val _loginFlow: MutableStateFlow<APIResult<LoginResponse>> = MutableStateFlow(APIResult.Idle(true))
    val loginFlow: StateFlow<APIResult<LoginResponse>> = _loginFlow
    val composite = CompositeDisposable()

    val logInResponse = flow {
        emit(APIResult.Loading(true))
        val response = enterpriseDataRepository.login(Credential("long.chen@eyeque.com","123456"))
        if(!response.isSuccessful){
            emit(APIResult.Error(response.message()))
        }else{
            emit(APIResult.Success(response.body()!!))
        }
    }.asLiveData(Dispatchers.IO)

    fun login(credential: Credential){
        viewModelScope.launch {
            _loginFlow.value = APIResult.Loading(true)
            enterpriseDataRepository.loginFlow(credential).catch {
                e -> _loginFlow.value = APIResult.Error(e.message!!)
            }.collect{
                Constants.token = it.token
                _loginFlow.value = APIResult.Success(it)
            }
        }
    }
    val testFlow = MutableStateFlow("")
    fun test(){
        val flow = flow<String> {
            emit("1");
            delay(100)
            emit("2")
        }.flowOn(Dispatchers.IO)

        viewModelScope.launch {
            flow.catch {
                    e -> e.message
            }.collect{
                testFlow.value = it
            }
        }
    }

    val job1Flow: MutableStateFlow<ResponseData> = MutableStateFlow(ResponseData("default",-1,"default"))
    fun workOnJobOne(){
        viewModelScope.launch {
           reactiveDataRepository.workOnJobOneFlow()
               .catch { e-> e.printStackTrace() }
               .collect{
                   job1Flow.emit(it)
               }
        }
    }
    val job2Flow: MutableStateFlow<ResponseData> = MutableStateFlow(ResponseData("default",-1,"default"))
    fun workOnJobTwo(){
        viewModelScope.launch {
            reactiveDataRepository.workOnJobTwoFlow()
                .catch { e -> e.printStackTrace() }
                .collect{
                    job2Flow.emit(it)
                }
        }
    }

    val job1LiveData = MutableLiveData<ResponseData>()
    fun workOnJob1(){
         viewModelScope.launch{
            reactiveDataRepository.workOnJob1Observable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        job1LiveData.postValue(it)
                    },
                    {
                        it.printStackTrace()
                    },
                    {

                    }
                )
        }
    }

    val job2LiveData = MutableLiveData<ResponseData>()
    fun workOnJob2(){
        viewModelScope.launch {
            composite.add(
                reactiveDataRepository.workOnJob2Observable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            job2LiveData.postValue(it)
                        },
                        {
                            it.printStackTrace()
                        },
                        {

                        })
            )
        }
    }
    val test2Flow = MutableStateFlow<String>("")

    fun workOnTest2(){
        viewModelScope.launch {
            flow<String>{
                val ans = test2()
                emit(ans)
            }.flowOn(Dispatchers.IO)
                .catch { e-> e.printStackTrace() }
                .collect{
                    test2Flow.value = it
                }
        }
    }

    suspend fun test2(): String{
        delay(1000)
        return "test2 job completed"
    }
}