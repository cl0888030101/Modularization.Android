package com.eyeque.blegatt

import android.bluetooth.BluetoothDevice

interface BleServiceInterface {
    fun connectDevice(bluetoothDevice: BluetoothDevice)
    fun disconnectDevice()
    fun writeData(byteArray: ByteArray)
}

interface BleGattListener{
    enum class BleConnectionStatus{
        GATT_CONNECT_FAILED,
        GATT_SUCCESS,
        GATT_CONNECTED,
        GATT_DISCONNECTED
    }
    //send out the connection device
    fun onConnectionChanged(bleConnectionStatus: BleConnectionStatus)
    fun onDataReceived(receivedData: ByteArray)
    fun onDataWritten(isSuccessful: Boolean)
}

