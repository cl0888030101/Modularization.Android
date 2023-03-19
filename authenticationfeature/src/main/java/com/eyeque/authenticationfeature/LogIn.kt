package com.eyeque.authenticationfeature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eyeque.authenticationfeature.ui.main.UserLogInFragment

class LogIn : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UserLogInFragment.newInstance())
                .commitNow()
        }
    }
}