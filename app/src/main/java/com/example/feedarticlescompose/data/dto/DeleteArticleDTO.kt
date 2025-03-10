package com.devid_academy.feedarticlescompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


class DeleteArticleDTO (
    @Json(name = "id")
    val id: Long,
)

