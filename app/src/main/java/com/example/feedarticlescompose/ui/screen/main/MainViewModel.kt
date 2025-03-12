package com.example.feedarticlescompose.ui.screen.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devid_academy.feedarticlescompose.data.api.ApiService
import com.devid_academy.feedarticlescompose.data.dto.ArticleDTO
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import com.devid_academy.feedarticlescompose.utils.ArticleEvent
import com.devid_academy.feedarticlescompose.utils.ArticleState
import com.example.feedarticlescompose.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _articles = MutableStateFlow<List<ArticleDTO>>(emptyList())
    val articles: StateFlow<List<ArticleDTO>> = _articles

    private val _selectedCategory = MutableStateFlow(0)
    val selectedCategory: StateFlow<Int> = _selectedCategory

    private val _currentUserId = MutableStateFlow<Long>(0)
    val currentUserId: StateFlow<Long> = _currentUserId

    private val _mainSharedFlow = MutableSharedFlow<String?>()
    val mainSharedFlow: SharedFlow<String?> = _mainSharedFlow

    val filteredArticles: StateFlow<List<ArticleDTO>> = combine(
        _articles, _selectedCategory
    ) { articles, selectedCategory ->
        if (selectedCategory == 0) {
            articles
        } else {
            articles.filter { it.category == selectedCategory }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        getArticles()
        _currentUserId.value = pm.getUserId()
    }
    fun getArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().getAllArticles()
                }
                if(response.isSuccessful){
                    val result = response.body()
                    Log.i("VM RV", "VM RV result : $result")
                    _articles.value = result!!
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
            _isLoading.value = false
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
            _selectedCategory.value = category
        }
    }


    fun navigateIfUserIsOwner(idUser: Long, articleId: Long) {
        if (_currentUserId.value == idUser) {
            viewModelScope.launch {
                _mainSharedFlow.emit(Screen.Edit.route + "/$articleId")
            }
        }
    }
}

