package com.ngoclong.testfcm

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private lateinit var FILE_NAME: String
    private lateinit var mPrefs: SharedPreferences

    private val KEY_TOKENS = "key_tokens"
    private val KEY_MY_TOKEN = "key_my_token"

    init {
        val context = MyApp.context
        FILE_NAME = context.packageName
        mPrefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    fun addNewToken(token: String) {
        var tokens = getTokens()
        if (!tokens.contains(token)) {
            tokens += token
        }
        saveTokens(tokens)
    }

    fun saveTokens(tokens: List<String>) {
        mPrefs.edit().putString(KEY_TOKENS, tokens.joinToString("|")).commit()
    }

    fun getTokens(): List<String> {
        val tokensString = mPrefs.getString(KEY_TOKENS, "")
        return if (tokensString != null && !tokensString.equals("")) {
            tokensString.split("|")
        } else listOf()
    }

    fun saveMyToken(token: String) {
        mPrefs.edit().putString(KEY_MY_TOKEN, token).commit()
    }

    fun getMyToken(): String? {
        val token = mPrefs.getString(KEY_MY_TOKEN, null)
        return if (token != null && !token.isEmpty()) token else null
    }
}