package com.example.feedarticlescompose.ui.screen.create

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlescompose.R
import com.devid_academy.feedarticlescompose.data.api.ApiService
import com.devid_academy.feedarticlescompose.data.dto.ArticleDTO
import com.devid_academy.feedarticlescompose.data.dto.CreaArticleDTO
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.devid_academy.feedarticlescompose.utils.ArticleState
import com.devid_academy.feedarticlescompose.utils.makeToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreaArticleViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val apiService: ApiService
): ViewModel() {

    private val _articlesState = MutableLiveData<ArticleState>(ArticleState.Idle)
    val articlesState: LiveData<ArticleState> = _articlesState

    fun addArticle(
        articleTitle: String,
        articleDesc: String,
        articleImageUrl: String,
        selectedValueForCategory: String
    ){
        if(articleTitle.isNotEmpty() && articleDesc.isNotEmpty() &&
            articleImageUrl.isNotEmpty() && selectedValueForCategory.isNotEmpty())
            viewModelScope.launch {
                _articlesState.value = ArticleState.Loading
                val userId = preferencesManager.getUserId()
                val catId = selectedValueForCategory.toInt()
                try {
                    val result = withContext(Dispatchers.IO) {
                        apiService.getApi().insertArticle(CreaArticleDTO(
                            articleTitle,
                            articleDesc,
                            articleImageUrl,
                            catId,
                            userId
                        ))
                    }
                    when (result.status) {
                        1 -> _articlesState.value = ArticleState.Success
                        0 ->  _articlesState.value = ArticleState.NoCreation
                        -1 -> _articlesState.value = ArticleState.ParamIssue
                        5 -> _articlesState.value = ArticleState.Forbidden
                    }
                } catch(e: Exception) {
                    Log.e("CreaArticleViewModel", "API call failed: ${e.localizedMessage}", e)
                }
                _articlesState.value = ArticleState.Idle
            }
        else
            _articlesState.value = ArticleState.Uncompleted
    }



}

