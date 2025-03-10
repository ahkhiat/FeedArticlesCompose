package com.devid_academy.feedarticlescompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


@Parcelize
class UpdateArticleDTO (
    @Json(name = "id")
    val id: Long,

    @Json(name = "title")
    val title: String,

    @Json(name = "desc")
    val description: String,

    @Json(name = "image")
    val urlImage: String,

    @Json(name = "cat")
    val category: Int,

) : Parcelable

