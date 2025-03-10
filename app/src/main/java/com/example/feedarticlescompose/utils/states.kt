package com.devid_academy.feedarticlescompose.utils

sealed class ArticleState {
    object Idle: ArticleState()
    object Loading : ArticleState()
    object Success : ArticleState()
    object NoCreation : ArticleState()
    object ParamIssue : ArticleState()
    object Forbidden : ArticleState()
    object Unchanged : ArticleState()
    object IdIssue : ArticleState()
    object Undeleted : ArticleState()
    object DeleteSuccess : ArticleState()
    object Uncompleted: ArticleState()
}

sealed class FavoriteState {
    object isFavorite: FavoriteState()
    object isNotFavorite: FavoriteState()
}