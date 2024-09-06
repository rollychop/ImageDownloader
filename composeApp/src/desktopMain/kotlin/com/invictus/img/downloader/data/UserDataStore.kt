package com.invictus.img.downloader.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.invictus.img.downloader.domain.model.user.UserModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.IOException


class UserDataStore(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val NAME_KEY = stringPreferencesKey("name.key")
        val EMAIL_KEY = stringPreferencesKey("email.key")
        val USERNAME_KEY = stringPreferencesKey("username.key")
        val MOBILE_NUMBER_KEY = stringPreferencesKey("mobile.number.key")
        val ID_KEY = stringPreferencesKey("id.key")
        val LOGIN_ID_KEY = stringPreferencesKey("login.id.key")
        val ROLE_KEY = stringPreferencesKey("role_key")
        val HEADER_KEY = stringPreferencesKey("header_key")
        val ICP_KEY = stringPreferencesKey("icp.key")
    }

    suspend fun getPrefixHeader(): String? = dataStore.data.map { it[HEADER_KEY] }
        .firstOrNull()

    suspend fun savePrefixHeader(header: String) {
        dataStore.edit { preferences ->
            preferences[HEADER_KEY] = header
        }
    }

    suspend fun getIcp(): String? = dataStore.data.map { it[ICP_KEY] }.firstOrNull()
    suspend fun saveIcp(icp: String) {
        dataStore.edit { preferences ->
            preferences[ICP_KEY] = icp
        }
    }

    suspend fun saveUserInfo(
        userModel: UserModel,
    ) {
        saveUserInfo(
            userModel.name,
            userModel.email,
            userModel.mobileNumber,
            userModel.username,
            userModel.id,
            userModel.loginId,
            userModel.role
        )
    }

    private suspend fun saveUserInfo(
        name: String,
        email: String,
        mobileNumber: String,
        username: String,
        id: String,
        loginId: String,
        role: String,
    ) {
        dataStore.edit {
            it[NAME_KEY] = name
            it[EMAIL_KEY] = email
            it[USERNAME_KEY] = username
            it[MOBILE_NUMBER_KEY] = mobileNumber
            it[ID_KEY] = id
            it[LOGIN_ID_KEY] = loginId
            it[ROLE_KEY] = role
        }
    }

    suspend fun getUserOrThrow(): UserModel {
        val userModelFlow = getUserFlow()
        return userModelFlow.first()
    }

    suspend fun getUserOrNull(): UserModel? = getUserFlow().firstOrNull()
    fun getUserFlow() = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            UserModel(
                name = preferences[NAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                username = preferences[USERNAME_KEY] ?: "",
                mobileNumber = preferences[MOBILE_NUMBER_KEY] ?: "",
                id = preferences[ID_KEY] ?: "",
                loginId = preferences[LOGIN_ID_KEY] ?: "",
                role = preferences[ROLE_KEY] ?: "user"
            )
        }

    suspend fun clear() {
        dataStore.edit { preferences -> preferences.clear() }
    }


}
