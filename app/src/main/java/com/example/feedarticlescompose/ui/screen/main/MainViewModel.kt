package com.example.feedarticlescompose.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.feedarticlescompose.data.api.ApiService
import com.devid_academy.feedarticlescompose.data.dto.ArticleDTO
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {
    private val _isLoadingStateFlow = MutableStateFlow(false)
    val isLoadingStateFlow: StateFlow<Boolean> = _isLoadingStateFlow

    private val _articlesListStateFlow = MutableStateFlow<List<ArticleDTO>>(emptyList())
    val articlesListStateFlow: StateFlow<List<ArticleDTO>> = _articlesListStateFlow

    private val _selectedCategoryStateFlow = MutableStateFlow(0)
    val selectedCategoryStateFlow: StateFlow<Int> = _selectedCategoryStateFlow

    private val _currentUserIdStateFlow = MutableStateFlow<Long>(0)
    val currentUserIdStateFlow: StateFlow<Long> = _currentUserIdStateFlow

    private val _mainSharedFlow = MutableSharedFlow<String?>()
    val mainSharedFlow: SharedFlow<String?> = _mainSharedFlow

    val filteredArticlesStateFlow: StateFlow<List<ArticleDTO>> = combine(
        _articlesListStateFlow, _selectedCategoryStateFlow
    ) { articles, selectedCategory ->
        if (selectedCategory == 0) {
            articles
        } else {
            articles.filter { it.category == selectedCategory }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        getArticles()
        _currentUserIdStateFlow.value = pm.getUserId()
    }
    fun getArticles() {
        viewModelScope.launch {
            _isLoadingStateFlow.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().getAllArticles()
                }
                if(response.isSuccessful){
                    val result = response.body()
                    Log.i("VM RV", "VM RV result : $result")
                    _articlesListStateFlow.value = result!!
                } else when(response.code()) {
                    401 -> {
                        Log.d("MAIN VM", "Main Vm erreur 401")
                        pm.removeToken()
                        _mainSharedFlow.emit(Screen.Login.route)
                    }
                    400 -> Log.d("MAIN VM", "Main Vm erreur 400 Erreur param")
                    500 -> Log.d("MAIN VM", "Main Vm erreur 500 Erreur Mysql")
                }
            } catch(e: Exception) {
                Log.e("MAIN VM", "Main VM undefined error : ${e.message}")
            }
            _isLoadingStateFlow.value = false
        }
    }

    fun deleteArticle(articleId: Long){
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().deleteArticle(articleId)
                }
                if (response.isSuccessful){
                    getArticles()
                    Log.i("VM EDIT", "VM EDIT deleteArticle : article has been deleted")
                } else when (response.code()) {
                    304 -> Log.d("VM EDIT", "Erreur 303 ids are differents")
                    400 -> Log.d("VM EDIT", "Erreur 400 probleme parametre")
                    401 -> Log.d("VM EDIT", "Erreur 401 unauthorized")
                    503 -> Log.d("VM EDIT", "Erreur 503 erreur mysql")
                }
            } catch(e: Exception) {
                Log.e("EDIT VM", "DELETE API call failed: ${e.localizedMessage}", e)
            }
        }
    }

    fun setSelectedCategory(category: Int) {
        viewModelScope.launch {
            delay(200)
            _selectedCategoryStateFlow.value = category
        }
    }


    fun navigateIfUserIsOwner(idUser: Long, articleId: Long) {
        if (_currentUserIdStateFlow.value == idUser) {
            viewModelScope.launch {
                _mainSharedFlow.emit(Screen.Edit.route + "/$articleId")
            }
        }
    }
}

