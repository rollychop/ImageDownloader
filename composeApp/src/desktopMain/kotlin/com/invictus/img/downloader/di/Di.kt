package com.invictus.img.downloader.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.invictus.img.downloader.data.UserDataStore
import com.invictus.img.downloader.data.repository.AuthRepositoryImpl
import com.invictus.img.downloader.data.repository.UserIdCardRepositoryImpl
import com.invictus.img.downloader.data.service.AuthService
import com.invictus.img.downloader.data.service.UserIdCardService
import com.invictus.img.downloader.domain.repository.AuthRepository
import com.invictus.img.downloader.domain.repository.UserIdCardRepository
import com.invictus.img.downloader.domain.use_case.auth.LogInUseCase
import com.invictus.img.downloader.domain.use_case.session.CheckLoggedInSession
import com.invictus.img.downloader.domain.use_case.user.GetLoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.Path.Companion.toPath
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object Di {


    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://api-data-collection.quivertech.in/")
            .build()
    }

    private val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    private val idCardService: UserIdCardService by lazy {
        retrofit.create(UserIdCardService::class.java)
    }


    private val gson by lazy {
        GsonBuilder().apply {
            addSerializationExclusionStrategy(
                object : ExclusionStrategy {
                    override fun shouldSkipField(f: FieldAttributes?): Boolean {
                        val exposes = f?.annotations?.filterIsInstance<Expose>()
                        val expose = exposes?.firstOrNull()
                        val serialize = expose?.serialize
                        return serialize?.not() ?: false
                    }

                    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                        return false
                    }
                }
            )
        }.create()
    }


    private val okHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(
            HttpLoggingInterceptor.Level.BODY
        )
        return@lazy OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()

                runBlocking {
                    userDataStore.getIcp()?.let {
                        request.addHeader("Cookie", it)
                    }
                }

                chain.proceed(request.build())
            }
            .addInterceptor { chain ->
                val originalResponse: Response = chain.proceed(chain.request())
                runBlocking(coroutineDispatcher()) {
                    val cookieList = Cookie.parseAll(chain.request().url, originalResponse.headers)
                    if (cookieList.isNotEmpty()) {
                        println(cookieList)
                        cookieList.firstOrNull { it.name.contains("icp.sid") }
                            ?.let {
                                userDataStore.saveIcp(it.name + "=" + it.value)
                                println(userDataStore.getIcp())
                            }
                    }
                }
                originalResponse
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }


    val userDataStore by lazy {
        UserDataStore(createDataStore { File(DATA_STORE_FILE_NAME).absolutePath })
    }

    private fun createDataStore(producePath: () -> String): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { producePath().toPath() }
        )

    private const val DATA_STORE_FILE_NAME: String = "dice.preferences_pb"
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            authService,
            coroutineDispatcher(),
            userDataStore = userDataStore
        )
    }

    val idCarRepository: UserIdCardRepository by lazy {
        UserIdCardRepositoryImpl(
            service = idCardService,
            ioDispatcher = coroutineDispatcher(),
            userDataStore = userDataStore,
            gson = gson,
        )
    }


    object UseCase {
        val loginUseCase by lazy { LogInUseCase(authRepository) }
        val checkLogInUseCase by lazy {
            CheckLoggedInSession(userDataStore)
        }

        val logoutUseCase = userDataStore::clear

        val getLoggedUser by lazy {
            GetLoggedUser(userDataStore)
        }
    }

    private fun coroutineDispatcher() = Dispatchers.IO


    fun dispose() {
    }


}
