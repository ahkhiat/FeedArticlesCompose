package com.devid_academy.feedarticlescompose.data.manager

import android.content.Context
import android.content.SharedPreferences
import com.devid_academy.feedarticlescompose.utils.SHARED_PREFS
import com.devid_academy.feedarticlescompose.utils.TOKEN
import com.devid_academy.feedarticlescompose.utils.USER_ID
import javax.inject.Inject

class PreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
){
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }
    fun setToken(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }
    fun removeToken() {
        sharedPreferences.edit().remove(TOKEN).apply()
    }
    fun getUserId(): Long {
        return sharedPreferences.getLong(USER_ID, -1L)
    }
    fun setUserId(id: Long) {
        sharedPreferences.edit().putLong(USER_ID, id).apply()
    }
}