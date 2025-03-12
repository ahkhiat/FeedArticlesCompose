package com.devid_academy.feedarticlescompose.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.devid_academy.feedarticlescompose.data.manager.AuthManager
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthManager,
): ViewModel() {

    private val _directionStateFlow = MutableStateFlow<String?>(null)
    val directionStateFlow: StateFlow<String?> = _directionStateFlow

    fun logout() {
        authManager.logout()
        _directionStateFlow.value = Screen.Login.route
    }
}