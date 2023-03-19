package com.eyeque.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BroadcastExample @Inject constructor(@ApplicationContext val context: Context) {
    val broadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {

        }
    }

    fun registerBroadCast(){
        context.registerReceiver(broadcastReceiver, IntentFilter("action1"))
    }

    fun unresigerBroadCast(){
        context.unregisterReceiver(broadcastReceiver)
    }

    fun sendBroadcast(){
        context.sendBroadcast(Intent("action1").also {
            it.putExtra("key1", "value1")
        })
    }
}