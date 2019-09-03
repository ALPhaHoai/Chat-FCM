package com.ngoclong.testfcm

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.lang.Exception

object FMCUtils {

    val TAG = "FMCUtils"

    fun getOtherToken(): String? {
        val tokens = PreferencesHelper.getTokens()
        val myToken = PreferencesHelper.getMyToken()
        val filterTokens = tokens.filter { token -> token != null && !token.equals(myToken) }
        return if (filterTokens.isEmpty()) null else filterTokens.first()
    }

    fun sendToken(token: String) {
        sendMessage("@@@New_User_Token: $token")
    }

    fun sendMessage(message: String) {
        val otherToken = getOtherToken()
        if (otherToken != null && !otherToken.isEmpty()) {
            SendMessage(message, otherToken).execute()
        } else {
            Log.d(TAG, "sendMessage: cannot find other token")
        }
    }
}

class SendMessage(val message: String, val otherToken: String) : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {

        Log.d(FMCUtils.TAG, "Sending message $message to $otherToken")
        val connection = Jsoup.connect("https://fcm.googleapis.com/fcm/send")
        with(connection) {
            ignoreContentType(true)
            method(Connection.Method.POST)
            header("Content-Type", "application/json")
            header("Authorization", "key=AIzaSyDQpEZunXgzp-kwi1BeATLRv8Pgc1ZsCFI")
            requestBody(
                """
                {
                 "to": "$otherToken",
                 "collapse_key" : "type_a",
                 "priority": "high",
                 "notification" : {
                     "body" : "$message",
                     "title": "Title of Your Notification"
                 },
                 "data" : {
                     "body" : "$message",
                     "title": "Title of Your Notification in Title",
                     "key_1" : "Value for key_1",
                     "key_2" : "Value for key_2"
                 }
                }
            """.trimIndent()
            )
        }
        try {
            connection.execute()
        } catch (e: Exception){
            Log.e(FMCUtils.TAG, e.toString())
        }
        return null
    }
}