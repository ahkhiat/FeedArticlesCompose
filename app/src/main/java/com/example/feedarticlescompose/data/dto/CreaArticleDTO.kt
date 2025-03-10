package com.devid_academy.feedarticlescompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class CreaArticleDTO (
    @Json(name = "title")
    val title: String,

    @Json(name = "desc")
    val description: String,

    @Json(name = "image")
    val urlImage: String,

    @Json(name = "cat")
    val category: Int,

    @Json(name = "id_u")
    val idUser: Long,
): Parcelable


