package com.ngoclong.testfcm

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = MyFirebaseMessagingService::class.simpleName!!
    var notificationManager: NotificationManagerCompat? = null


    override fun onCreate() {
        super.onCreate()

        notificationManager = NotificationManagerCompat.from(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        FMCUtils.sendToken(token)
        PreferencesHelper.saveMyToken(token)

        Log.d(TAG, "NEW_TOKEN $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "NEW_NESSAGE $remoteMessage")

        if (remoteMessage.notification?.body?.startsWith("@@@New_User_Token: ") ?: false) {
            remoteMessage.notification?.body?.removePrefix("@@@New_User_Token: ")?.let {
                PreferencesHelper.addNewToken(it)
            }
        } else {
            notificationManager?.let {
                val mBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ad_title)
                    .setContentTitle(remoteMessage.notification?.title)
                    .setContentText(remoteMessage.notification?.body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                it.notify(0, mBuilder.build());
            }
        }
    }
}