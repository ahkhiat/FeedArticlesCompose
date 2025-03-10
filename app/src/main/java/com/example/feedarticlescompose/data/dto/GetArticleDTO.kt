package com.devid_academy.feedarticlescompose.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetArticleDTO(
    @Json(name = "status")
    val status: String,

    @Json(name = "article")
    val article: ArticleDTO
)