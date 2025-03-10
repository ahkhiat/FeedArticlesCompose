package com.devid_academy.feedarticlescompose.data.dto.auth

import com.squareup.moshi.Json

data class AuthDTO (
    @Json(name = "login")
    val name: String,

    @Json(name = "mdp")
    val password: String,
)
