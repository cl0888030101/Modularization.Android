package com.eyeque.blegatt

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BleGattRepository @Inject constructor(private val bleGattService: BleGattService) {
    var bleConnectionStatusFlow: Flow<BleGattListener.BleConnectionStatus>? = null
    init {
        bleGattService.bleGattListener = object : BleGattListener{
            override fun onConnectionChanged(bleConnectionStatus: BleGattListener.BleConnectionStatus) {
                bleConnectionStatusFlow = flow{
                    emit(bleConnectionStatus)
                }.flowOn(Dispatchers.IO)

                Observable.create{
                    it.onNext(bleConnectionStatus)
                    it.onComplete()
                }
            }

            override fun onDataReceived(receivedData: ByteArray) {
                flow{
                    emit(receivedData)
                }.flowOn(Dispatchers.IO)
            }

            override fun onDataWritten(isSuccessful: Boolean) {

            }
        }
    }

    fun connect(){

    }

    fun disconnect(){

    }

    fun writeData(){

    }
}