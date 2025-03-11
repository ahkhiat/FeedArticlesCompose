package com.devid_academy.feedarticlescompose.ui.screen.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.feedarticlescompose.data.api.ApiService
import com.devid_academy.feedarticlescompose.data.dto.auth.AuthDTO
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.example.feedarticlescompose.R
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
class RegisterViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _registerStateFlow = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerStateFlow: StateFlow<RegisterState> = _registerStateFlow

    private val _registerSharedFlow = MutableSharedFlow<AuthEvent?>()
    val registerSharedFlow: SharedFlow<AuthEvent?> = _registerSharedFlow

    fun register(email: String, password: String, passwordConfirm: String) {
        _registerStateFlow.value = RegisterState.Loading
            viewModelScope.launch {
                if (email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()) {
                    if (password == passwordConfirm) {
                        try {
                            val response = withContext(Dispatchers.IO) {
                                api.getApi().registerUser(AuthDTO(email, password))
                            }
                            Log.i("VM REGISTER", "Response : $response")
                            if (response.isSuccessful) {
                                val result = response.body()
                                if (result?.token != null) {
                                    pm.setToken(result.token)
                                    pm.setUserId(result.id)
                                }
                                _registerStateFlow.value = RegisterState.Success
                                _registerSharedFlow.emit(AuthEvent.NavigateToMainScreen)
                            } else when (response.code()) {
                                303 -> {
                                    Log.i("VM LOGIN", "Erreur 303 login deja utilisé")
                                    _registerStateFlow.value = RegisterState.UsernameAlreadyExists
                                    _registerSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.invalid_credentials))
                                }

                                304 -> {
                                    Log.i("VM LOGIN", "Erreur 304 compte non crée")
                                    _registerStateFlow.value = RegisterState.NotCreated
                                    _registerSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.login_successful_with_security_issue))
                                }

                                400 -> {
                                    Log.i("VM LOGIN", "Erreur 400 pb de parametre")
                                    _registerStateFlow.value = RegisterState.Error
                                    _registerSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.undefined_error))
                                }

                                503 -> {
                                    Log.i("VM LOGIN", "Erreur 503 erreur Mysql")
                                    _registerStateFlow.value = RegisterState.Error
                                    _registerSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.undefined_error))
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("VM Register", "Undefined error : ${e.message}")
                        }
                    } else {
                        _registerStateFlow.value = RegisterState.PasswordsDifferent
                        _registerSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.passwords_differents))
                    }
                } else {
                    _registerStateFlow.value = RegisterState.Incomplete
                    _registerSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.fill_all_inputs))
                }
            }
    }
    fun resetRegisterState() {
        _registerStateFlow.value = RegisterState.Idle
    }
}

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Incomplete : RegisterState()
    data object Loading : RegisterState()
    data object Success : RegisterState()
    data object UsernameAlreadyExists: RegisterState()
    data object PasswordsDifferent: RegisterState()
    data object NotCreated : RegisterState()
    data object Error : RegisterState()
}
