package com.devid_academy.feedarticlescompose.data.api


import javax.inject.Inject

class ApiService @Inject constructor(
    private val apiInterface: ApiInterface
    ) {
        fun getApi(): ApiInterface = apiInterface
        }