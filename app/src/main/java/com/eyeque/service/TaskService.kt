package com.eyeque.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*


class TaskService: Service() {
    val corutineScope = CoroutineScope(Dispatchers.IO)
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.getStringExtra("InoutService")){

        }
        corutineScope.launch {
            delay(1000)
            val outIntent = Intent("TaskServce")
            outIntent.putExtra("key1", "value1")
            sendBroadcast(outIntent)
        }
        return START_STICKY
    }
}