@file:Suppress("unused")

package com.invictus.img.downloader.data.repository

import com.google.gson.Gson
import com.invictus.img.downloader.data.UserDataStore
import com.invictus.img.downloader.data.dto.OrganisationDto
import com.invictus.img.downloader.data.mapper.toModel
import com.invictus.img.downloader.data.mapper.user.toModel
import com.invictus.img.downloader.data.service.UserIdCardService
import com.invictus.img.downloader.domain.model.OrganisationModel
import com.invictus.img.downloader.domain.model.user.IdCardUserModel
import com.invictus.img.downloader.domain.repository.UserIdCardRepository
import com.invictus.img.downloader.util.AppFileHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.time.Duration
import kotlin.math.abs


class UserIdCardRepositoryImpl(
    private val service: UserIdCardService,
    private val ioDispatcher: CoroutineDispatcher,
    private val userDataStore: UserDataStore,
    private val gson: Gson,

    ) : UserIdCardRepository {


    private fun romanToInt(s: String): Int {
        val orNull = s.toIntOrNull()
        if (orNull != null) return orNull
        val romanMap = hashMapOf(
            'I' to 1, 'V' to 5, 'X' to 10,
            'L' to 50, 'C' to 100, 'D' to 500,
            'M' to 1000
        )
        var result = 0
        var prevValue = 0
        for (i in s.length - 1 downTo 0) {
            val value = romanMap[s[i]] ?: 0
            if (value < prevValue)
                result -= value
            else
                result += value
            prevValue = value
        }
        return result
    }


    override suspend fun getOnlineClassList()
            : Result<List<String>> = runCatchingCustom {
        service.getClassList().getBodyOrThrow()
            .sortedBy(::romanToInt)
    }

    override suspend fun getOnlineSectionList(classId: String)
            : Result<List<String>> = runCatchingCustom {
        service.getSectionList(classId).getBodyOrThrow().sorted()
    }


    override suspend fun getClassList(): Result<List<String>> {
        return runCatchingCustom {
            service.getClassList().getBodyOrThrow()
        }.map { it.sortedBy(::romanToInt) }
    }

    override suspend fun getSectionList(classId: String): Result<List<String>> {
        return runCatchingCustom {
            service.getSectionList(classId).getBodyOrThrow()
        }
    }


    override suspend fun loadEnterUsersIntoLocalDB(): Result<Unit> {
        return runCatchingCustom {}
    }

    override fun getLastDataSyncedAt(): Flow<String> = flow {
        emit("")
    }

    override suspend fun getOnlineUsers(
        classId: String,
        sectionId: String
    ): Result<List<IdCardUserModel>> = runCatchingCustom {
        val response = service.getAllUsers(
            gson.toJson(mutableMapOf<String, Any?>().apply {
                put("class", arrayOf(classId))
                put("section", arrayOf(sectionId))
            }).toJsonRequestBody()
        )
        val data = response.getBodyOrThrow().map { it.toModel() }
        data
    }

    @Volatile
    private var lastUserData: List<IdCardUserModel> = emptyList()

    @Volatile
    private var lastClassSectionId = "" to ""


    override suspend fun getUsers(
        classId: String,
        sectionId: String,
        cached: Boolean
    ): Result<List<IdCardUserModel>> = runCatchingCustom {
        if (classId.isBlank() || sectionId.isBlank())
            return@runCatchingCustom emptyList()
        if (cached && lastClassSectionId == (classId to sectionId)) {
            return@runCatchingCustom lastUserData
        }

        getOnlineUsers(classId, sectionId).getOrThrow()
    }

    override suspend fun getUsers(prefix: String?): Result<List<IdCardUserModel>> = runCatchingCustom {

        service.getAllUsers("{}".toJsonRequestBody(), prefix).getBodyOrThrow()
            .map { it.toModel() }
    }


    override suspend fun generateSingleUserToken(loginId: String): Result<String> =
        runCatchingCustom {
            service.getUserToken(loginId).getDataOrThrow()
        }


    private fun getLoginId(anyMap: Map<String, Any?>): String? {
        val loginId = anyMap["login_id"]
        if (loginId is Number) {
            return loginId.toLong().toString()
        }
        return loginId?.toString()
    }


    override suspend fun saveUserImage(
        contentUri: String,
        loginId: String,
    ): Result<Any> = runCatchingCustom {

    }

    override suspend fun getMyOrganisation(): Result<OrganisationModel> {
        val prefix = userDataStore.getPrefixHeader()

        return getOrganisation(prefix).mapCatching { organisationModels ->
            val model = organisationModels.firstOrNull()
                ?: throw IllegalStateException("Organisation not defined")
            model
        }
    }

    private class Organisations(
        val updated: Long,
        val data: List<OrganisationDto>
    )

    /** @param prefix if passed null, then returns all organizations */
    override suspend fun getOrganisation(prefix: String?): Result<List<OrganisationModel>> =
        runCatchingCustom {


            val filePath = AppFileHelper.getCacheDirectory()

            val cacheFile = File(filePath, "organisations.json")
            if (cacheFile.exists() && cacheFile.length() > 0) {
                val org = gson.fromJson(
                    cacheFile.readText(),
                    Organisations::class.java
                )

                if (abs(System.currentTimeMillis() - org.updated) < Duration.ofHours(1).toMillis())
                    return@runCatchingCustom org.data.map { it.toModel() }
            }

            service.getOrganisation(prefix).getBodyOrThrow()
                .also { list ->
                    val g = gson.toJson(Organisations(System.currentTimeMillis(), list))
                    cacheFile.writeText(g)
                }
                .map { it.toModel() }
        }

}
