package com.devid_academy.feedarticlescompose.data.manager

import javax.inject.Inject

class AuthManager @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    fun logout() {
        preferencesManager.removeToken()
    }
}