package com.ngoclong.testfcm

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics

class MyApp : Application() {
    companion object {
        lateinit var context: Context
    }

    val TAG = MyApp::class.simpleName!!
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        Log.d(TAG, "oncreate")
        FirebaseApp.initializeApp(baseContext);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        startService(Intent(this, MyFirebaseMessagingService::class.java))
    }
}