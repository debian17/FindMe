package ru.debian17.findme.data.manager

import android.content.Context

class AccessTokenManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "prefsName"
        private const val ACCESS_TOKEN_KEY = "accessTokenKey"
        private const val DEFAULT_ACCESS_TOKEN = "defaultAccessToken"
    }

    private val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val sharedPrefEditor = sharedPref.edit()

    fun setAccessToken(accessToken: String) {
        sharedPrefEditor.putString(ACCESS_TOKEN_KEY, accessToken).apply()
    }

    fun getAccessToken(): String {
        val accessToken = sharedPref.getString(
                ACCESS_TOKEN_KEY,
                DEFAULT_ACCESS_TOKEN
        )
        return accessToken ?: DEFAULT_ACCESS_TOKEN
    }

    fun isUserLogin(): Boolean {
        val accessToken = sharedPref.getString(
                ACCESS_TOKEN_KEY,
                DEFAULT_ACCESS_TOKEN
        )
        return !(accessToken == null || accessToken == DEFAULT_ACCESS_TOKEN)
    }

    fun logout() {
        sharedPrefEditor.putString(ACCESS_TOKEN_KEY, DEFAULT_ACCESS_TOKEN).apply()
    }

}