package com.invictus.img.downloader.data.service

import com.invictus.img.downloader.data.dto.IdCardUserDto
import com.invictus.img.downloader.data.dto.OrganisationDto
import ins.quivertech.app.data.data_source.remote.dto.CResponse
import ins.quivertech.app.data.data_source.remote.dto.SelfVerifyDto
import ins.quivertech.app.data.data_source.remote.dto.UserPageWrapperDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface UserIdCardService {

    @POST("v1/users/all")
    suspend fun getAllUsers(
        @Body body: RequestBody,
        @Header("prefix") prefix: String? = null
    ): Response<List<IdCardUserDto>>

    @POST("v1/users/all-by-pagination")
    suspend fun getAllUsersPaginated(@Body body: RequestBody): Response<UserPageWrapperDto>

    @POST("v1/users/all")
    suspend fun getAllDynamicUsers(@Body body: RequestBody): Response<List<Map<String, Any>>>


    @GET("v1/users/generate-edit-token")
    suspend fun getUserToken(
        @Header("login_id") loginId: String,
    ): Response<CResponse<String>>


    @GET("v1/users/verify-edit-token")
    suspend fun getUserDetails(
        @Header("edt") token: String,
    ): Response<CResponse<SelfVerifyDto>>


    @PUT("v1/users/{loginId}")
    suspend fun updateUser(
        @Path("loginId") loginId: String,
        @Body body: RequestBody,
        @Header("edt") token: String? = null,
    ): Response<Any>


    @POST("v1/users")
    suspend fun saveUser(@Body body: RequestBody): Response<CResponse<IdCardUserDto>>


    @GET("v1/users/class")
    suspend fun getClassList(): Response<List<String>>

    @GET("v1/users/section")
    suspend fun getSectionList(@Query("class") classId: String): Response<List<String>>


    @POST("v1/users/profile-pic")
    suspend fun saveUserImage(@Body body: RequestBody): Response<Any>


    @GET("/organisation")
    suspend fun getOrganisation(
        @Query("prefix") prefix: String? = null
    ): Response<List<OrganisationDto>>

    @GET("/organisation/user-dashboard")
    suspend fun getDashboardStats(): Response<CResponse<Map<String, Any?>>>


}
