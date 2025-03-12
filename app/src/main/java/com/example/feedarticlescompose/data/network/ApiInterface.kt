package com.devid_academy.feedarticlescompose.data.api

import com.devid_academy.feedarticlescompose.data.dto.ArticleDTO
import com.devid_academy.feedarticlescompose.data.dto.CreaArticleDTO
import com.devid_academy.feedarticlescompose.data.dto.UpdateArticleDTO
import com.devid_academy.feedarticlescompose.data.dto.auth.AuthDTO
import com.devid_academy.feedarticlescompose.data.dto.auth.StatusAuthDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiRoutes.LOGIN_USER)
    suspend fun loginUser(
        @Field("login") name: String,
        @Field("mdp") password: String
    ): Response<StatusAuthDTO>

    @PUT(ApiRoutes.REGISTER_USER)
    suspend fun registerUser(@Body user: AuthDTO): Response<StatusAuthDTO>

    @GET(".")
    suspend fun getAllArticles(): Response<List<ArticleDTO>?>

    @GET(ApiRoutes.GET_ARTICLE)
    suspend fun getArticle(@Path("id") articleId: Long): Response<ArticleDTO?>

    @PUT(".")
    suspend fun insertArticle(@Body article: CreaArticleDTO): Response<Unit>

    @POST(ApiRoutes.UPDATE_ARTICLE)
    suspend fun updateArticle(@Path("id") articleId: Long, @Body updateArticle: UpdateArticleDTO): Response<Unit>

    @DELETE(ApiRoutes.DELETE_ARTICLE)
    suspend fun deleteArticle(@Path("id") articleId: Long): Response<Unit>

}