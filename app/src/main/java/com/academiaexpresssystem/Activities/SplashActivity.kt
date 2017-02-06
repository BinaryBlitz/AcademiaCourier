package com.academiaexpresssystem.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.academiaexpresssystem.R
import com.academiaexpresssystem.Server.DeviceInfoStore
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.splash_layout)
        openActivity(if (DeviceInfoStore.getToken(this) == "null") AuthActivity::class.java else OrdersActivity::class.java)
    }

    private fun openActivity(activity: Class<*>) {
        val intent = Intent(this@SplashActivity, activity)
        startActivity(intent)
        finish()
    }
}