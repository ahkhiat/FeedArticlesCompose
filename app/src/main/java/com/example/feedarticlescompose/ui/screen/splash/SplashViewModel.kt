package com.devid_academy.feedarticlescompose.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): ViewModel() {
    private val _isLoadingStateFlow = MutableStateFlow(false)
    val isLoadingStateFlow: StateFlow<Boolean> = _isLoadingStateFlow

    private val _goToMainOrLogin = MutableSharedFlow<String?>()
    val goToMainOrLogin = _goToMainOrLogin.asSharedFlow()

    init {
        verifyToken()
    }

    fun verifyToken() {
        val token = preferencesManager.getToken()
        viewModelScope.launch {
            delay(300)
            _isLoadingStateFlow.value = true
            delay(3000)
            _goToMainOrLogin.emit(
                if(token.isNullOrEmpty())
                    Screen.Login.route
                else
                    Screen.Main.route
            )
            _isLoadingStateFlow.value = false
        }
    }
}