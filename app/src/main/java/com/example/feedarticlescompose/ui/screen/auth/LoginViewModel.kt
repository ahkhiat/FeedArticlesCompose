package com.devid_academy.feedarticlescompose.ui.screen.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.feedarticlescompose.data.api.ApiService
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.devid_academy.feedarticlescompose.utils.AuthEvent
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
class LoginViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _loginSharedFlow = MutableSharedFlow<AuthEvent?>()
    val loginSharedFlow: SharedFlow<AuthEvent?> = _loginSharedFlow

    fun verifyLogin(email: String, password: String) {
        viewModelScope.launch {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                try {
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
                        _loginSharedFlow.emit(AuthEvent.NavigateToMainScreen)
                    } else when (response.code()) {
                        401 -> {
                            Log.i("VM LOGIN", "Erreur 401 User inconnu")
                            _loginSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.invalid_credentials))
                        }
                        304 -> {
                            Log.i("VM LOGIN", "Erreur 304 Ok mais token inchangé")
                            _loginSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.login_successful_with_security_issue))
                        }
                        400 -> {
                            Log.i("VM LOGIN", "Erreur 400 pb de parametre")
                            _loginSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.undefined_error))
                        }
                        503 -> {
                            Log.i("VM LOGIN", "Erreur 503 erreur Mysql")
                            _loginSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.undefined_error))
                        }
                    }
                } catch (e: Exception) {
                    Log.e("VM Login", "Undefined error : ${e.message}")
                    _loginSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.undefined_error))
                }
            } else {
                _loginSharedFlow.emit(AuthEvent.ShowSnackBar(R.string.fill_all_inputs))
            }
        }
    }
}

