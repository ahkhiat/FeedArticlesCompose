package com.example.feedarticlescompose.ui.screen.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.feedarticlescompose.data.api.ApiService
import com.devid_academy.feedarticlescompose.data.dto.ArticleDTO
import com.devid_academy.feedarticlescompose.data.dto.CreaArticleDTO
import com.devid_academy.feedarticlescompose.data.dto.UpdateArticleDTO
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import com.devid_academy.feedarticlescompose.utils.ArticleState
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.ui.screen.create.ArticleEvent
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
class EditArticleViewModel @Inject constructor(
    private val pm: PreferencesManager,
    private val api: ApiService
): ViewModel() {

    private val _editStateFlow = MutableStateFlow<ArticleState>(ArticleState.Idle)
    val editStateFlow: StateFlow<ArticleState> = _editStateFlow

    private val _articleStateFlow = MutableStateFlow<ArticleDTO?>(null)
    val articleStateFlow: StateFlow<ArticleDTO?> = _articleStateFlow

    private val _editSharedFlow = MutableSharedFlow<ArticleEvent?>()
    val editSharedFlow: SharedFlow<ArticleEvent?> = _editSharedFlow

    fun getArticle(articleId: Long) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().getArticle(articleId)
                }
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.i("VM EDIT", "VM EDIT getArticle result : $result")
                    _articleStateFlow.value = result
                } else when(response.code()) {
                    303 -> {
                        Log.i("VM EDIT", "Erreur 303 article introuvable")
                        _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.article_not_found))
                    }
                    400 -> {
                        Log.i("VM EDIT", "Erreur 400 probleme parametre")
                        _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.edit_param_issue))
                    }
                    401 -> {
                        Log.i("VM EDIT", "Erreur 401 user inconnu mauvais token")
                        _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.edit_forbidden))
                    }
                    503 -> {
                        Log.i("VM EDIT", "Erreur 503 erreur mysql")
                        _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.server_error))
                    }
                }
            } catch(e: Exception) {
                Log.e("EDIT VM", "EDIT getArticle API call failed: ${e.localizedMessage}", e)
            }
        }
    }

    fun updateArticle(
        articleId: String,
        articleTitle: String,
        articleDesc: String,
        articleImageUrl: String,
        selectedValueForCategory: String
    ){
        if(articleTitle.isNotEmpty() && articleDesc.isNotEmpty() &&
            articleImageUrl.isNotEmpty() && selectedValueForCategory.isNotEmpty())

            viewModelScope.launch {
                _editStateFlow.value = ArticleState.Loading
                val userId = pm.getUserId()
                val catId = selectedValueForCategory.toInt()
                try {
                    val response = withContext(Dispatchers.IO) {
                        api.getApi().updateArticle(
                            articleId.toLong(),
                            UpdateArticleDTO(
                            articleId.toLong(),
                            articleTitle,
                            articleDesc,
                            articleImageUrl,
                            catId
                        ))
                    }
                    if (response.isSuccessful) {
                        Log.i("VM EDIT", "VM EDIT updateArticle : article has been updated")
                        _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.edit_success))
                        _editSharedFlow.emit(ArticleEvent.NavigateToMainScreen)
                    } else when(response.code()) {
                        303 -> {
                            Log.i("VM EDIT", "Erreur 303 ids are differents")
                            _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.edit_forbidden))
                        }
                        400 -> {
                            Log.i("VM EDIT", "Erreur 400 probleme parametre")
                            _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.edit_param_issue))
                        }
                        401 -> {
                            Log.i("VM EDIT", "Erreur 401 unauthorized")
                            _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.edit_forbidden))
                        }
                        503 -> {
                            Log.i("VM EDIT", "Erreur 503 erreur mysql")
                            _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.server_error))
                        }
                    }
                } catch(e: Exception) {
                    Log.e("EDIT VM", "EDIT updateArticle API call failed: ${e.localizedMessage}", e)
                }
                _editStateFlow.value = ArticleState.Idle
            }
        else
            _editStateFlow.value = ArticleState.Uncompleted
    }
    fun deleteArticle(articleId: Long){
        viewModelScope.launch {
            _editStateFlow.value = ArticleState.Loading
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().deleteArticle(articleId)
                }
                if (response.isSuccessful){
                    Log.i("VM EDIT", "VM EDIT deleteArticle : article has been deleted")
                    _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.delete_success))
                } else when (response.code()) {
                    304 -> {
                        Log.i("VM EDIT", "Erreur 303 ids are differents")
                        _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.undeleted))
                    }
                    400 -> {
                        Log.i("VM EDIT", "Erreur 400 probleme parametre")
                        _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.edit_param_issue))
                    }
                    401 -> {
                        Log.i("VM EDIT", "Erreur 401 unauthorized")
                        _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.delete_forbidden))
                    }
                    503 -> {
                        Log.i("VM EDIT", "Erreur 503 erreur mysql")
                        _editSharedFlow.emit(ArticleEvent.ShowSnackBar(R.string.server_error))
                    }
                }
            } catch(e: Exception) {
                Log.e("EDIT VM", "DELETE API call failed: ${e.localizedMessage}", e)
            }
            _editStateFlow.value = ArticleState.Idle
        }
    }
}


