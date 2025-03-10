package com.devid_academy.feedarticlescompose.ui.screen.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.feedarticlescompose.data.api.ApiService
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _loginStateFlow = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginStateFlow

    private val _directionSharedFlow = MutableSharedFlow<String?>()
    val directionSharedFlow: SharedFlow<String?> = _directionSharedFlow

    fun verifyLogin(email: String, password: String) {
        _loginStateFlow.value = LoginState.Loading
        if (email.isNotEmpty() && password.isNotEmpty()) {
            try {
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        api.getApi().loginUser(email, password)
                    }
                    Log.i("VM LOGIN", "Response : $response")
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result?.token != null) {
                            pm.setToken(result.token)
                            pm.setUserId(result.id)
                        }
                        _loginStateFlow.value = LoginState.Success
                        _directionSharedFlow.emit(Screen.Main.route)
                    } else when (response.code()) {
                        401 -> Log.i("VM LOGIN", "Erreur 401 User inconnu")
                        304 -> Log.i("VM LOGIN", "Erreur 304 Ok mais token inchangé")
                        400 -> Log.i("VM LOGIN", "Erreur 400 pb de parametre")
                        503 -> Log.i("VM LOGIN", "Erreur 503 erreur Mysql")
                    }
                }

            } catch (e: Exception) {
                Log.e("VM Login", "Undefined error : ${e.message}")
            }
        } else {
            _loginStateFlow.value = LoginState.Incomplete
        }
    }

    fun resetLoginState() {
        _loginStateFlow.value = LoginState.Idle
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Incomplete : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data object Invalid : LoginState()
    data object Error: LoginState()
}
