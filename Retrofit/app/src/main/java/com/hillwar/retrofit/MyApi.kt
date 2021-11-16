package com.hillwar.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface MyApi {

    @GET("/posts")
    suspend fun getListOfPosts(): List<Post>

    @POST("/posts")
    suspend fun createPost(@Body data: Post): Response<ResponseBody>

    @DELETE("/posts/{id}")
    suspend fun deleteGist(@Path("id") id: String): Response<ResponseBody>

}