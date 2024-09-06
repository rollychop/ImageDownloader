package com.invictus.img.downloader.data.repository


import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import ins.quivertech.app.data.data_source.remote.dto.CResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException


val gson by lazy { Gson() }


val log: Logger = LoggerFactory.getLogger("NetworkUtil")

fun String.getJsonTree(): JsonElement =
    if (this.isBlank()) JsonNull.INSTANCE else try {
        gson.fromJson(this, JsonElement::class.java)
    } catch (e: MalformedJsonException) {
        JsonNull.INSTANCE
    }


fun <T> Response<T>.getBodyOrThrow(): T {
    check(isSuccessful) {
        val errorBody = errorBody()?.string()?.getJsonTree()
        var message: String? = ""
        if (errorBody?.isJsonObject == true) {
            val error = errorBody.asJsonObject.get("error").asString
            val meg = errorBody.asJsonObject.get("message").asString
            message = "$error : $meg"
            if (error.isBlank() || (error.equals("true") || error.equals("false"))) {
                message = meg
            }

            if (error.isBlank() && meg.isBlank()) {
                message = null
            }
        }
        message ?: message()
    }
    return checkNotNull(body()) {
        "No body received from server"
    }
}

fun <T> Response<CResponse<T>>.getDataOrThrow(): T {
    val cResponse = getBodyOrThrow()
    return checkNotNull(cResponse.data) {
        cResponse.message ?: "No data received from server"
    }
}

inline fun <T, R> T.runCatchingCustom(block: T.() -> R): Result<R> {
    return try {
        val value = block()
        Result.success(value)
    } catch (e: Exception) {
        log.error("Error in runCatchingCustom", e)
        if (e is CancellationException) throw e
        if (e is HttpException) {
            if (e.code() == 401) return Result.failure(
                Exception(
                    "You're not unauthorized to access. " +
                            "Please check login status"
                )
            )
            return Result.failure(Exception("Http Exception"))
        }
        if (e is UnknownHostException) {
            return Result.failure(
                UnknownHostException(
                    "Unable to resolve server host. " +
                            "Please contact support"
                )
            )
        }

        if (e is ConnectException) {
            return Result.failure(
                ConnectException(
                    "Unable to connect to the server. " +
                            "Please try again later"
                )
            )
        }

        if (e is SocketTimeoutException) {
            return Result.failure(
                SocketTimeoutException(
                    "Server is taking longer time than expected. " +
                            "Please check your connection or try again later"
                )
            )
        }
        if (e is MalformedJsonException || e is JsonSyntaxException) {
            return Result.failure(Exception("Failed to resolve response"))
        }

        if (e is IOException) {
            return Result.failure(Exception("Connection error! Please check your connection or try again later"))
        }


        Result.failure(e)
    }
}

val ApplicationJSONMediaType = "application/json".toMediaTypeOrNull()
fun String.toJsonRequestBody(): RequestBody = toRequestBody(ApplicationJSONMediaType)


