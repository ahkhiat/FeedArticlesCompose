package com.devid_academy.feedarticlescompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class ArticleDTO (
    @Json(name = "id")
    val id: Long,

    @Json(name = "titre")
    val title: String,

    @Json(name = "descriptif")
    val description: String,

    @Json(name = "url_image")
    val urlImage: String,

    @Json(name = "categorie")
    val category: Int,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "id_u")
    val idUser: Long? = null,

): Parcelable


