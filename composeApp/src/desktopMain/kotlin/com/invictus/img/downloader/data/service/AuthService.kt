package com.invictus.img.downloader.data.service


import ins.quivertech.app.data.data_source.remote.dto.AuthResponseDto
import ins.quivertech.app.data.data_source.remote.dto.CResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body body: RequestBody): Response<CResponse<AuthResponseDto>>

    @POST("auth/signup")
    suspend fun register(@Body body: RequestBody): Response<CResponse<AuthResponseDto>>

    @GET("auth/logout")
    suspend fun logout(): Response<CResponse<Any>>

    @POST("auth/forget_password")
    suspend fun forgetPassword(@Body body: RequestBody): Response<CResponse<Any>>

    @GET("auth/profile")
    suspend fun profile(): Response<CResponse<AuthResponseDto>>


}
