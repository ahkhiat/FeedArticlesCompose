package com.example.feedarticlescompose.ui.screen.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.feedarticlescompose.data.api.ApiService
import com.devid_academy.feedarticlescompose.data.dto.CreaArticleDTO
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.devid_academy.feedarticlescompose.ui.screen.auth.AuthEvent
import com.devid_academy.feedarticlescompose.ui.screen.auth.LoginState
import com.devid_academy.feedarticlescompose.utils.ArticleState
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
class CreaArticleViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val apiService: ApiService
): ViewModel() {

    private val _createStateFlow = MutableStateFlow<ArticleState>(ArticleState.Idle)
    val createStateFlow: StateFlow<ArticleState> = _createStateFlow

    private val _createSharedFlow = MutableSharedFlow<ArticleEvent?>()
    val createSharedFlow: SharedFlow<ArticleEvent?> = _createSharedFlow

    fun addArticle(
        articleTitle: String,
        articleDesc: String,
        articleImageUrl: String,
        selectedValueForCategory: String
    ) {
        viewModelScope.launch {
            if (
                articleTitle.isNotEmpty() &&
                articleDesc.isNotEmpty() &&
                articleImageUrl.isNotEmpty() &&
                selectedValueForCategory.isNotEmpty()
            ) {
                _createStateFlow.value = ArticleState.Loading
                val userId = preferencesManager.getUserId()
                val catId = selectedValueForCategory.toInt()
                try {
                    val response = withContext(Dispatchers.IO) {
                        apiService.getApi().insertArticle(
                            CreaArticleDTO(
                                articleTitle,
                                articleDesc,
                                articleImageUrl,
                                catId,
                                userId
                            )
                        )
                    }
                    Log.i("VM CREATE", "Response : $response")
                    if (response.isSuccessful) {
                        _createStateFlow.value = ArticleState.Success
                        _createSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.create_success))
                        _createSharedFlow.emit(ArticleEvent.NavigateToMainScreen)
                    } else when (response.code()) {
                        401 -> {
                            Log.i("VM LOGIN", "Erreur 401 Creation non autorisée mauvais token")
                            _createStateFlow.value = ArticleState.Forbidden
                            _createSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.create_forbidden))
                        }
                        304 -> {
                            Log.i("VM LOGIN", "Erreur 304 article non crée")
                            _createStateFlow.value = ArticleState.NoCreation
                            _createSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.create_no_creation))
                        }
                        400 -> {
                            Log.i("VM LOGIN", "Erreur 400 pb de parametre")
                            _createStateFlow.value = ArticleState.ParamIssue
                            _createSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.undefined_error))
                        }
                        503 -> {
                            Log.i("VM LOGIN", "Erreur 503 erreur Mysql")
                            _createStateFlow.value = ArticleState.Error
                            _createSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.undefined_error))
                        }
                    }
                } catch (e: Exception) {
                    Log.e("CreaArticleViewModel", "API call failed: ${e.localizedMessage}", e)
                }
            } else {
                _createStateFlow.value = ArticleState.Uncompleted
                _createSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.fill_all_inputs))
            }
        }
    }

}

sealed class ArticleEvent {
    data object NavigateToMainScreen: ArticleEvent()
    data class ShowSnackBar(val resId: Int): ArticleEvent()
}