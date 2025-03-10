package com.devid_academy.feedarticlescompose.data.dto.auth


import com.squareup.moshi.Json

data class StatusAuthDTO(
    @Json(name = "status")
    val status: Int?,

    @Json(name = "id")
    val id: Long,

    @Json(name = "token")
    val token: String?
)