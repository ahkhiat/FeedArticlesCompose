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
class MainViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _articles = MutableStateFlow<List<ArticleDTO>>(emptyList())
    val articles: StateFlow<List<ArticleDTO>> = _articles

    private val _filteredArticles = MutableStateFlow<List<ArticleDTO>>(emptyList())
    val filteredArticles: StateFlow<List<ArticleDTO>> = _filteredArticles

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Idle)
    val sessionState: StateFlow<SessionState> = _sessionState

    private val _currentUserId = MutableStateFlow<Long>(0)
    val currentUserId: StateFlow<Long> = _currentUserId

    private val _directionSharedFlow = MutableSharedFlow<String?>()
    val directionSharedFlow: SharedFlow<String?> = _directionSharedFlow


    var hasShownLoginToast = false
    private var isFiltered = false

    init {
        getArticles()
        _currentUserId.value = pm.getUserId()
    }
    fun getArticles() {
        viewModelScope.launch {
            _sessionState.value = SessionState.Checking
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
                        _directionSharedFlow.emit(Screen.Login.route)
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
//    fun getFilteredArticles(categoryId: Int) {
//        _isLoading.value = true
//        if(categoryId == 0)
//            _filteredArticles.value = _articles.value
//        else
//            _filteredArticles.value = _articles.value?.filter {
//                it.category == categoryId
//            }
//        _isLoading.value = false
//    }

//    fun navigateIfUserIsOwner(article: ArticleDTO): NavDirections {
//        val userId = _currentUserId.value
//        return if (userId == article.idUser)
//            MainFragmentDirections.actionMainFragmentToEditArticleFragment(article)
//        else
//            MainFragmentDirections.actionMainFragmentToDetailsArticleFragment(article)
//    }
}
sealed class SessionState {
    data object Idle : SessionState()
    data object Checking: SessionState()
    data object Unauthorized : SessionState()
    data object Authorized: SessionState()
}

