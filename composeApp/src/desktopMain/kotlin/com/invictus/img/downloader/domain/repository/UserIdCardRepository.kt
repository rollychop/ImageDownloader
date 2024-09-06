package com.invictus.img.downloader.domain.repository

import com.invictus.img.downloader.domain.model.OrganisationModel
import com.invictus.img.downloader.domain.model.user.IdCardUserModel
import kotlinx.coroutines.flow.Flow


typealias MessageText = String
typealias ClassName = String
typealias SectionName = String
typealias Token = String

interface UserIdCardRepository {

    suspend fun getOnlineClassList(): Result<List<ClassName>>

    suspend fun getOnlineSectionList(classId: String): Result<List<SectionName>>

    suspend fun getOnlineUsers(
        classId: String,
        sectionId: String
    ): Result<List<IdCardUserModel>>

    suspend fun getUsers(
        classId: String,
        sectionId: String,
        cached: Boolean = false
    ): Result<List<IdCardUserModel>>

    suspend fun getUsers(prefix: String?): Result<List<IdCardUserModel>>


    suspend fun generateSingleUserToken(loginId: String): Result<Token>


    suspend fun saveUserImage(
        contentUri: String,
        loginId: String
    ): Result<Any>


    suspend fun getMyOrganisation(): Result<OrganisationModel>

    suspend fun loadEnterUsersIntoLocalDB(): Result<Unit>
    fun getLastDataSyncedAt(): Flow<String>
    suspend fun getClassList(): Result<List<String>>
    suspend fun getSectionList(classId: String): Result<List<String>>

    /** @param prefix if passed null, then returns all organizations */
    suspend fun getOrganisation(prefix: String?): Result<List<OrganisationModel>>
}
