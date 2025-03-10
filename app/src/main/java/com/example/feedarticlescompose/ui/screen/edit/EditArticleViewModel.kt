package com.example.feedarticlescompose.ui.screen.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.feedarticlescompose.data.api.ApiService
import com.devid_academy.feedarticlescompose.data.dto.CreaArticleDTO
import com.devid_academy.feedarticlescompose.data.dto.UpdateArticleDTO
import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import com.devid_academy.feedarticlescompose.utils.ArticleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditArticleViewModel @Inject constructor(
    private val pm: PreferencesManager,
    private val api: ApiService
): ViewModel() {

    private val _articlesState = MutableLiveData<ArticleState>(ArticleState.Idle)
    val articlesState: LiveData<ArticleState> = _articlesState

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
                _articlesState.value = ArticleState.Loading
                val userId = pm.getUserId()
                val catId = selectedValueForCategory.toInt()
                try {
                    val result = withContext(Dispatchers.IO) {
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
                    when (result?.status) {
                        1 -> _articlesState.value = ArticleState.Success
                        0 ->  _articlesState.value = ArticleState.Unchanged
                        -1 -> _articlesState.value = ArticleState.ParamIssue
                        -2 -> _articlesState.value = ArticleState.IdIssue
                        5 -> _articlesState.value = ArticleState.Forbidden
                    }
                } catch(e: Exception) {
                    Log.e("EditArticleViewModel", "API call failed: ${e.localizedMessage}", e)
                }
                _articlesState.value = ArticleState.Idle
            }
        else
            _articlesState.value = ArticleState.Uncompleted
    }
    fun deleteArticle(articleId: String){
        viewModelScope.launch {
            _articlesState.value = ArticleState.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    api.getApi().deleteArticle(articleId.toLong())
                }
                when (result?.status) {
                    1 -> _articlesState.value = ArticleState.DeleteSuccess
                    0 ->  _articlesState.value = ArticleState.Undeleted
                    -1 -> _articlesState.value = ArticleState.ParamIssue
                    5 -> _articlesState.value = ArticleState.Forbidden
                }
            } catch(e: Exception) {
                Log.e("DeleteArticleViewModel", "API call failed: ${e.localizedMessage}", e)
            }
            _articlesState.value = ArticleState.Idle
        }
    }
}


