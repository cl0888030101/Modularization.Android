package com.eyeque.blegatt

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BroadcastChannel
import java.util.*
import javax.inject.Inject

class BleGattService @Inject constructor (@ApplicationContext private val context: Context): BleServiceInterface {

    companion object{
        const val GATT_SERVICE_STRING = "your serice string"
        const val CHARACTERISTIC_READ_STRING = "your read string"
        const val CHARACTERISTIC_WRITE_STRING = "your read string"
    }

    var bleGattListener: BleGattListener? = null
    private var bluetoothDevice: BluetoothDevice? = null
    private var bleGatt: BluetoothGatt? = null

    private val bleGattCallback: BluetoothGattCallback = object : BluetoothGattCallback(){

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when(status){
                BluetoothGatt.GATT_FAILURE -> {
                    bleGattListener?.onConnectionChanged(BleGattListener.BleConnectionStatus.GATT_CONNECT_FAILED)
                    disconnectDevice()
                }
                BluetoothGatt.GATT_SUCCESS -> {
                    bleGattListener?.onConnectionChanged(BleGattListener.BleConnectionStatus.GATT_SUCCESS)
                }
                BluetoothGatt.STATE_CONNECTED -> {
                    bleGattListener?.onConnectionChanged(BleGattListener.BleConnectionStatus.GATT_CONNECTED)
                }
                BluetoothGatt.STATE_DISCONNECTED -> {
                    bleGattListener?.onConnectionChanged(BleGattListener.BleConnectionStatus.GATT_DISCONNECTED)
                }
                else -> {}
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            writeDescriptor()
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            when(characteristic.uuid){
                UUID.fromString(CHARACTERISTIC_READ_STRING) ->{
                    bleGattListener?.onDataReceived(value)
                }
            }

        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            bleGattListener?.onDataWritten(status == 1)
        }
    }

    override fun connectDevice(bluetoothDevice: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        this.bluetoothDevice = bluetoothDevice
        bluetoothDevice.connectGatt(context, false, bleGattCallback)
    }

    override fun disconnectDevice() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bleGatt?.let {
            bluetoothDevice = null
            it.disconnect()
            it.close()
        }
    }

    override fun writeData(byteArray: ByteArray) {
        //find characteristic

        //cmdCharacteristic.value = bytearray
    }

    private fun writeDescriptor(){

    }

    private fun sendBroadcast(){
        context.sendBroadcast(Intent("forFunAction").apply {
            this.putExtra("forFun", false)
        })
        context.registerReceiver(receiver, IntentFilter().apply {
            this.addAction("forFunAction")
        })
    }
    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            p1?.getBooleanExtra("forfun", false)
        }
    }
}