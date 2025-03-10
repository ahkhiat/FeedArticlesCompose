package com.devid_academy.feedarticlescompose.data.dto


import com.squareup.moshi.Json

data class StatusDTO(
    @Json(name = "status")
    val status: Int
)